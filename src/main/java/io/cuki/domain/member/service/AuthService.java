package io.cuki.domain.member.service;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.jwt.TokenProvider;
import io.cuki.domain.member.exception.MemberAlreadyExistException;
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


    // 회원가입 1단계 - 메일주소 중복확인
    public Boolean duplicateEmailAddressForSignUp(DuplicateEmailAddressForSignUpRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new MemberAlreadyExistException("이미 가입되어 있는 유저입니다.");
        }

        return true;
    }

    // 회원가입 2단계 - 인증코드 전송
    public Boolean SendVerificationCodeForSignUp(SendVerificationCodeCodeForSignUpRequestDto requestDto) throws Exception {
        emailService.sendMessageForSignUp(requestDto.getEmail());

        return true;
    }

    // 회원가입 최종
    @Transactional
    public MemberInfoResponseDto signup(SignUpRequestDto signUpRequestDto) {
        // 1. 인증코드 검증
        emailService.verifyCode(signUpRequestDto.getEmail(), signUpRequestDto.getVerificationCode());

        // 2. 닉네임 랜덤 생성
        String nickname = Member.CreateRandomNickname();
        while (memberRepository.existsByNickname(nickname)){
            nickname = Member.CreateRandomNickname();
        }

        // 3. 멤버 객체 저장
        Member member = signUpRequestDto.toMember(passwordEncoder, nickname);

        return MemberInfoResponseDto.of(memberRepository.save(member));
    }

    // 로그인 1단계 - 메일주소 존재 여부
    public Boolean existEmailAddress(ExistEmailAddressForLoginRequestDto requestDto) {
        if (!memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("존재하지 않는 회원입니다. 메일 주소를 다시 한번 확인해 주세요.");
        }

        return true;
    }

    // 로그인 2단계 - 인증코드 전송
    public Boolean SendVerificationCodeForLogin(SendVerificationCodeForLoginRequestDto requestDto) throws Exception {
        emailService.sendMessageForLogin(requestDto.getEmail());

        return true;
    }

    // 로그인 최종
    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        // 1. 인증코드 검증
        emailService.verifyCode(loginRequestDto.getEmail(), loginRequestDto.getVerificationCode());

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
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. AccessToken에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID를 기반으로 Refresh Token 값 가져옴
        log.info("AuthService - authentication.getName() = {}", authentication.getName());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResponseDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenResponseDto;
    }

    // 로그아웃 - 리프레쉬 토큰 값 삭제
    @Transactional
    public Boolean logout() {
        refreshTokenRepository.deleteById(SecurityUtil.getCurrentMemberId());

        return true;
    }
}

