package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.LoginRequestDto;
import com.cuki.dto.TokenRequestDto;
import com.cuki.dto.TokenResponseDto;
import com.cuki.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인
    @PostMapping("/auth")
    public ApiResponse<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponse.ok(authService.login(loginRequestDto));
    }

    // 토큰 재발급
    @PostMapping("/auth/reissue")
    public ApiResponse<TokenResponseDto> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        return ApiResponse.ok(authService.reissue(tokenRequestDto));
    }
}
