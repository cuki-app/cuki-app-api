package com.cuki.service;

import com.cuki.controller.dto.*;
import com.cuki.entity.Member;
import com.cuki.entity.RefreshToken;
import com.cuki.jwt.TokenProvider;
import com.cuki.repository.MemberRepository;
import com.cuki.repository.RefreshTokenRepository;
import com.cuki.util.SecurityUtil;
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


    // 회원가입 1단계 - 메일주소 중복확인 및 인증코드 전송
    public Boolean duplicateEmailAddressForSignUp(DuplicateEmailAddressForSignUpRequestDto duplicateEmailAddressForSignUpRequestDto) throws Exception {
        String email = duplicateEmailAddressForSignUpRequestDto.getEmail();

        // 1. 메일주소 중복확인
        if (memberRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 2. 해당 메일주소로 인증코드 전송
        emailService.sendMessageForSignUp(email);

        return true;
    }

    // 회원가입 최종
    @Transactional
    public MemberInfoResponseDto signup(SignUpRequestDto signUpRequestDto) {
        // 1. 인증코드 검증
        emailService.verifyCode(signUpRequestDto.getEmail(), signUpRequestDto.getVerificationCode());

        // 2. 멤버 객체 저장
        Member member = signUpRequestDto.toMember(passwordEncoder);

        return MemberInfoResponseDto.of(memberRepository.save(member));
    }

    // 로그인 1단계 - 메일주소 존재 여부 및 인증코드 전송
    public Boolean existEmailAddress(ExistEmailAddressForLoginRequestDto existEmailAddressForLoginRequestDto) throws Exception {
        String email = existEmailAddressForLoginRequestDto.getEmail();

        // 1. 메일주소 존재 여부
        if (!memberRepository.existsByEmail(email)) {
            throw new RuntimeException("존재하지 않는 회원입니다. 메일 주소를 다시 한번 확인해 주세요.");
        }

        // 2. 해당 메일주소로 인증코드 전송
        emailService.sendMessageForLogin(email);

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

        log.info("리프레쉬토큰 저장 시 authentication.getName() : " + authentication.getName());

        // 5. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
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
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
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

    // 로그아웃 - 리프레쉬 토큰 값 변경
    @Transactional
    public Boolean logout() {
        RefreshToken refreshToken = refreshTokenRepository.findByKey(String.valueOf(SecurityUtil.getCurrentMemberId()))
                        .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        refreshToken.updateValue("logged out");
        refreshTokenRepository.save(refreshToken);

        return true;
    }
}
