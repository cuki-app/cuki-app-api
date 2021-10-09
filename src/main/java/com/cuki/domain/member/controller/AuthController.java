package com.cuki.domain.member.controller;

import com.cuki.global.common.response.ApiResponse;
import com.cuki.domain.member.dto.*;
import com.cuki.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 1단계 - 이메일 중복확인
    @PostMapping("/members/sign-up/email")
    public ApiResponse<Boolean> DuplicateEmailAddress(@RequestBody DuplicateEmailAddressForSignUpRequestDto requestDto) {
        return ApiResponse.ok(authService.duplicateEmailAddressForSignUp(requestDto));
    }

    // 회원가입 2단계 - 인증번호 발송
    @PostMapping("/members/sign-up/verifyCode")
    public ApiResponse<Boolean> SendVerifyCodeForSignUp(@RequestBody SendVerifyCodeForSignUpRequestDto requestDto) throws Exception {
        return ApiResponse.ok(authService.SendVerifyCodeForSignUp(requestDto));
    }

    // 회원가입 최종
    @PostMapping("/members/sign-up")
    public ApiResponse<MemberInfoResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ApiResponse.ok(authService.signup(signUpRequestDto));
    }

    // 로그인 1단계 - 회원 존재 여부 확인
    @PostMapping("/auth/login/email")
    public ApiResponse<Boolean> existEmailAddress(@RequestBody ExistEmailAddressForLoginRequestDto requestDto) {
        return ApiResponse.ok(authService.existEmailAddress(requestDto));
    }

    // 로그인 2단계 - 인증번호 발송
    @PostMapping("/auth/login/verifyCode")
    public ApiResponse<Boolean> SendVerifyCodeForLogin(@RequestBody SendVerifyCodeForLoginRequestDto requestDto) throws Exception {
        return ApiResponse.ok(authService.SendVerifyCodeForLogin(requestDto));
    }

    // 로그인 - 최종
    @PostMapping("/auth/login")
    public ApiResponse<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponse.ok(authService.login(loginRequestDto));
    }

    // 토큰 재발급
    @PostMapping("/auth/reissue")
    public ApiResponse<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ApiResponse.ok(authService.reissue(tokenRequestDto));
    }

    // 로그아웃
    @PostMapping("/auth/logout")
    public ApiResponse<Boolean> logout() {
        return ApiResponse.ok(authService.logout());
    }
}

