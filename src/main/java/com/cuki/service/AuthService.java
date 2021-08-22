package com.cuki.service;

import com.cuki.dto.LoginRequestDto;
import com.cuki.dto.TokenRequestDto;
import com.cuki.dto.TokenResponseDto;
import com.cuki.entity.RefreshToken;
import com.cuki.jwt.TokenProvider;
import com.cuki.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        // 1. Login ID(email)/PW 기반으로 AthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 2. 실제 검증이 이루어지는 단계
        // authenticate 메소드가 실행될 때 CustomUserDetailService의 loadUserByUsername 메소드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 AccessToken과 RefreshToken 생성
        TokenResponseDto tokenResponseDto = tokenProvider.createToken(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponseDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResponseDto;
    }

    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. RefreshToken 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. AccessToken에서 User ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 User ID를 기반으로 Refresh Token 값 가져옴
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
}
