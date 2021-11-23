package io.cuki.domain.member.service;

import io.cuki.domain.member.entity.*;
import io.cuki.domain.member.entity.jwt.TokenProvider;
import io.cuki.domain.member.exception.*;
import io.cuki.infra.email.EmailService;
import io.cuki.domain.member.dto.*;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.member.repository.RefreshTokenRepository;
import io.cuki.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final MemberService memberService;

    // 사용자 존재 유무 검사
    public Boolean existsEmailAddress(String email) {
        return memberRepository.existsByEmail(new Email(email));
    }

    // 회원가입 - 인증코드 전송
    @Transactional
    public Boolean sendVerificationCodeForSignUp(SendVerificationCodeForSignUpRequestDto requestDto) {
        String email = requestDto.getEmail();

        // 1. 이메일 검증
        Email.checkValidEmail(email);

        // 2. 인증코드 전송 (가입되어있는 사용자가 아닐 경우)
        if (!existsEmailAddress(email)) {
            emailService.sendMessageForSignUp(email);
        } else {
            log.error("{} -> 이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.", requestDto.getEmail());
            throw new MemberAlreadyExistException("이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.");
        }
        return true;
    }

    // 회원가입 최종
    @Transactional
    public MemberInfoResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        final String email = signUpRequestDto.getEmail();

        // 1. 이메일 검증
        Email.checkValidEmail(email);

        // 2. 인증코드 검증
        if (!existsEmailAddress(email)) {
            emailService.verifyCode(email, signUpRequestDto.getVerificationCode());
        } else {
            log.error("{} -> 이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.", email);
            throw new MemberAlreadyExistException("이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.");
        }

        // 3. 랜덤 닉네임 가져오기
        Nickname nickname = memberService.getRandomNickname();
        if (memberRepository.existsByNickname(nickname)) {
            log.error("회원가입 최종 단계 - 이미 존재하는 랜덤 닉네임입니다. -> {}", nickname.getNickname());
            throw new NicknameAlreadyExistException("다시 한번 눌려주세요.");
        }

        // 4. 멤버 객체 저장
        Member member = Member.builder()
                .email(new Email(email))
                .password(passwordEncoder.encode("1234"))
                .nickname(nickname)
                .activated(true)
                .authority(Authority.ROLE_USER)
                .build();

        return MemberInfoResponseDto.of(memberRepository.save(member));
    }

    // 로그인 - 인증코드 전송
    @Transactional
    public Boolean sendVerificationCodeForLogin(SendVerificationCodeForLoginRequestDto requestDto) {
        final String email = requestDto.getEmail();

        if (existsEmailAddress(email)) {
            emailService.sendMessageForLogin(email);
        } else {
            log.error("{} -> 존재하지 않는 회원입니다. 비정상적인 접근입니다.", requestDto.getEmail());
            throw new MemberNotFoundException("존재하지 않는 회원입니다. 비정상적인 접근입니다.");
        }
        return true;
    }

    // 로그인 최종
    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        final String email = loginRequestDto.getEmail();

        // 1. 인증코드 검증
        if (existsEmailAddress(email)) {
            emailService.verifyCode(email, loginRequestDto.getVerificationCode());
        } else {
            log.error("{} -> 존재하지 않는 회원입니다. 비정상적인 접근입니다.", email);
            throw new MemberNotFoundException("존재하지 않는 회원입니다. 비정상적인 접근입니다.");
        }

        // 2. Login ID(email)/PW 기반으로 AthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthenticationToken();

        // 3. 실제 검증이 이루어지는 단계
        // authenticate 메소드가 실행될 때 CustomUserDetailService의 loadUserByUsername 메소드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 4. 인증 정보를 기반으로 AccessToken과 RefreshToken 생성
        TokenResponseDto tokenResponseDto = tokenProvider.createToken(authentication);

        // 5. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(Long.valueOf(authentication.getName()))
                .value(tokenResponseDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 6. 토큰 발급
        return tokenResponseDto;
    }

    // 토큰 재발급
    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. RefreshToken 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RefreshTokenNotValidException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. AccessToken에서 Member 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new MemberAlreadyLoggedOutException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RefreshTokenNotMatchException("Refresh Token이 DB에 저장되어 있는 사용자의 토큰과 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트
        refreshToken.updateValue(tokenResponseDto.getRefreshToken());

        return tokenResponseDto;
    }

    // 로그아웃 - 리프레쉬 토큰 삭제
    @Transactional
    public Boolean logout() {
        try {
            refreshTokenRepository.deleteById(SecurityUtil.getCurrentMemberId());
        } catch (EmptyResultDataAccessException e) {
            throw new RefeshTokenNotFoundException(SecurityUtil.getCurrentMemberId() + " -> 해당 사용자의 Refresh Token은 존재하지 않습니다.");
        }

        return true;
    }


    // 회원탈퇴
    @Transactional
    public SuccessfullyDeletedMemberResponseDto withdrawal(Long memberId) {
        if (!memberId.equals(SecurityUtil.getCurrentMemberId())) {
            log.error("현재 로그인한 회원의 정보와 시큐리티 컨텍스트에 저장되어 있는 회원 정보가 일치하지 않습니다.");
            throw new MemberNotMatchException("현재 로그인한 회원의 정보와 시큐리티 컨텍스트에 저장되어 있는 회원 정보가 일치하지 않습니다.");
        }
        try {
            memberRepository.deleteById(memberId);
        } catch (EmptyResultDataAccessException e) {
            log.error("{} -> 삭제하려는 사용자의 정보를 데이터베이스에서 불러올 수 없습니다.", memberId);
            throw new MemberNotFoundException("삭제하려는 사용자의 정보를 데이터베이스에서 불러올 수 없습니다.");
        }

        return SuccessfullyDeletedMemberResponseDto.builder()
                .id(memberId)
                .build();
    }
}

