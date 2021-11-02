package io.cuki.domain.member.service;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.jwt.TokenProvider;
import io.cuki.domain.member.exception.*;
import io.cuki.infra.email.EmailService;
import io.cuki.domain.member.dto.*;
import io.cuki.domain.member.entity.RefreshToken;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.member.repository.RefreshTokenRepository;
import io.cuki.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    // 메일주소 존재 여부 확인
    public Boolean existsEmailAddress(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원가입 - 인증코드 전송
    public Boolean sendVerificationCodeForSignUp(SendVerificationCodeCodeForSignUpRequestDto requestDto) throws Exception {
        if (!existsEmailAddress(requestDto.getEmail())) {
            emailService.sendMessageForSignUp(requestDto.getEmail());
            return true;
        } else {
            log.debug("{} -> 이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.", requestDto.getEmail());
            throw new MemberAlreadyExistException("이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.");
        }
    }

    // 회원가입 최종
    @Transactional
    public MemberInfoResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        final String email = signUpRequestDto.getEmail();

        // 1. 인증코드 검증
        if (!existsEmailAddress(email)) {
            emailService.verifyCode(email, signUpRequestDto.getVerificationCode());
        } else {
            log.debug("{} -> 이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.", email);
            throw new MemberAlreadyExistException("이미 가입되어 있는 유저입니다. 비정상적인 접근입니다.");
        }

        // 2. 닉네임 랜덤 생성
        String nickname = Member.createRandomNickname();
        while (memberRepository.existsByNickname(nickname)){
            nickname = Member.createRandomNickname();
        }

        // 3. 멤버 객체 저장
        Member member = signUpRequestDto.toMember(passwordEncoder, nickname);

        return MemberInfoResponseDto.of(memberRepository.save(member));
    }

    // 로그인 - 인증코드 전송
    public Boolean sendVerificationCodeForLogin(SendVerificationCodeForLoginRequestDto requestDto) throws Exception {
        final String email = requestDto.getEmail();

        if (existsEmailAddress(email)) {
            emailService.sendMessageForLogin(email);
            return true;
        } else {
            log.debug("{} -> 존재하지 않는 회원입니다. 비정상적인 접근입니다.", requestDto.getEmail());
            throw new MemberNotFoundException("존재하지 않는 회원입니다. 비정상적인 접근입니다.");
        }
    }

    // 로그인 최종
    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        final String email = loginRequestDto.getEmail();

        // 1. 인증코드 검증
        if (existsEmailAddress(email)) {
            emailService.verifyCode(email, loginRequestDto.getVerificationCode());
        } else {
            log.debug("{} -> 존재하지 않는 회원입니다. 비정상적인 접근입니다.", email);
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

    // 로그아웃 - 리프레쉬 토큰 값 삭제
    @Transactional
    public Boolean logout() {
        refreshTokenRepository.deleteById(SecurityUtil.getCurrentMemberId());

        return true;
    }
}

