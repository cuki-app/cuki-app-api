package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.controller.dto.LoginRequestDto;
import com.cuki.controller.dto.TokenRequestDto;
import com.cuki.controller.dto.TokenResponseDto;
import com.cuki.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 로그인
    @PostMapping("/login")
    public ApiResponse<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponse.ok(authService.login(loginRequestDto));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ApiResponse<TokenResponseDto> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        return ApiResponse.ok(authService.reissue(tokenRequestDto));
    }
}
