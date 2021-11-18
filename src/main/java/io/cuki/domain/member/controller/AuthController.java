package io.cuki.domain.member.controller;

import io.cuki.domain.member.dto.*;
import io.cuki.domain.member.exception.MemberAlreadyExistException;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.member.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원 및 인증 관련 API"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "회원가입 1단계 - 이메일 중복확인")
    @PostMapping("/members/sign-up/email")
    public ApiResponse<Boolean> duplicateEmailAddressForSignUp(@RequestBody ValidateDuplicateEmailAddressForSignUpRequestDto requestDto) {
        if (authService.existsEmailAddress(requestDto.getEmail())) {
            log.error("{} -> 이미 가입되어 있는 유저입니다.", requestDto.getEmail());
            throw new MemberAlreadyExistException("이미 가입되어 있는 유저입니다.");
        }
        return ApiResponse.ok(true);
    }

    @ApiOperation(value = "회원가입 2단계 - 인증번호 발송")
    @PostMapping("/members/sign-up/verification-code")
    public ApiResponse<Boolean> sendVerificationCodeForSignUp(@RequestBody SendVerificationCodeCodeForSignUpRequestDto requestDto) {
        return ApiResponse.ok(authService.sendVerificationCodeForSignUp(requestDto));
    }

    @ApiOperation(value = "회원가입 최종")
    @PostMapping("/members/sign-up")
    public ApiResponse<MemberInfoResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ApiResponse.ok(authService.signUp(signUpRequestDto));
    }

    @ApiOperation(value = "로그인 1단계 - 회원 여부 체크")
    @PostMapping("/auth/login/email")
    public ApiResponse<Boolean> existsEmailAddress(@RequestBody ValidateExistEmailAddressForLoginRequestDto requestDto) {
        if (!authService.existsEmailAddress(requestDto.getEmail())) {
            log.error("{} -> 존재하지 않는 회원입니다. 메일 주소를 다시 한번 확인해 주세요.", requestDto.getEmail());
            throw new MemberNotFoundException("존재하지 않는 회원입니다. 메일 주소를 다시 한번 확인해 주세요.");
        }
        return ApiResponse.ok(true);
    }

    @ApiOperation(value = "로그인 2단계 - 인증번호 발송")
    @PostMapping("/auth/login/verification-code")
    public ApiResponse<Boolean> sendVerificationCodeForLogin(@RequestBody SendVerificationCodeForLoginRequestDto requestDto) {
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

    @ApiOperation(value = "회원탈퇴")
    @DeleteMapping("/members/{memberId}")
    public ApiResponse<SuccessfullyDeletedMemberResponseDto> withdrawal(@PathVariable Long memberId) {
        return ApiResponse.ok(authService.withdrawal(memberId));
    }
}

