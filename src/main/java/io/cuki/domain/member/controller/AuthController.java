package io.cuki.domain.member.controller;

import io.cuki.domain.member.dto.*;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.member.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"회원 인증 관련 API"})
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "회원가입 1단계 - 이메일 중복확인")
    @PostMapping("/members/sign-up/email")
    public ApiResponse<Boolean> duplicateEmailAddressForSignUp(@RequestBody DuplicateEmailAddressForSignUpRequestDto requestDto) {
        return ApiResponse.ok(authService.duplicateEmailAddressForSignUp(requestDto));
    }

    @ApiOperation(value = "회원가입 2단계 - 인증번호 발송")
    @PostMapping("/members/sign-up/verification-code")
    public ApiResponse<Boolean> sendVerificationCodeForSignUp(@RequestBody SendVerificationCodeCodeForSignUpRequestDto requestDto) throws Exception {
        return ApiResponse.ok(authService.sendVerificationCodeForSignUp(requestDto));
    }

    @ApiOperation(value = "회원가입 최종")
    @PostMapping("/members/sign-up")
    public ApiResponse<MemberInfoResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ApiResponse.ok(authService.signUp(signUpRequestDto));
    }

    @ApiOperation(value = "로그인 1단계 - 회원 여부 체크")
    @PostMapping("/auth/login/email")
    public ApiResponse<Boolean> existEmailAddress(@RequestBody ExistEmailAddressForLoginRequestDto requestDto) {
        return ApiResponse.ok(authService.existEmailAddress(requestDto));
    }

    @ApiOperation(value = "로그인 2단계 - 인증번호 발송")
    @PostMapping("/auth/login/verification-code")
    public ApiResponse<Boolean> sendVerificationCodeForLogin(@RequestBody SendVerificationCodeForLoginRequestDto requestDto) throws Exception {
        return ApiResponse.ok(authService.sendVerificationCodeForLogin(requestDto));
    }

    @ApiOperation(value = "로그인 최종")
    @PostMapping("/auth/login")
    public ApiResponse<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponse.ok(authService.login(loginRequestDto));
    }

    @ApiOperation(value = "토큰 재발급")
    @PostMapping("/auth/reissue")
    public ApiResponse<TokenResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ApiResponse.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "로그아웃")
    @PostMapping("/auth/logout")
    public ApiResponse<Boolean> logout() {
        return ApiResponse.ok(authService.logout());
    }
}

