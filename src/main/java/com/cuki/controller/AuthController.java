package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.controller.dto.*;
import com.cuki.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 - 이메일 중복확인
    @PostMapping("/members/sign-up/email")
    public ApiResponse<Boolean> DuplicateEmailAddress(@RequestBody DuplicateEmailAddressForSignUpRequestDto duplicateEmailAddressForSignUpRequestDto) throws Exception {
        return ApiResponse.ok(authService.duplicateEmailAddressForSignUp(duplicateEmailAddressForSignUpRequestDto));
    }

    // 회원가입 - 최종
    @PostMapping("/members/sign-up")
    public ApiResponse<MemberInfoResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ApiResponse.ok(authService.signup(signUpRequestDto));
    }

    // 로그인 - 회원 존재 여부 확인
    @PostMapping("/auth/login/email")
    public ApiResponse<Boolean> existEmailAddress(@RequestBody ExistEmailAddressForLoginRequestDto existEmailAddressForLoginRequestDto) throws Exception {
        return ApiResponse.ok(authService.existEmailAddress(existEmailAddressForLoginRequestDto));
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
}
