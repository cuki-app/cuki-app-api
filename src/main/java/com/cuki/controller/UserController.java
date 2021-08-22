package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.UserRequestDto;
import com.cuki.dto.UserResponseDto;
import com.cuki.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/users/sign-up")
    public ApiResponse<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ApiResponse.ok(userService.signup(userRequestDto));
    }

    @GetMapping("/users/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<UserResponseDto> getMyUserInfo() {
        return ApiResponse.ok(userService.getMyInfo());
    }

    @GetMapping("/users/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<UserResponseDto> getUserInfo(@PathVariable String email) {
        return ApiResponse.ok(userService.getUserInfo(email));
    }
}
