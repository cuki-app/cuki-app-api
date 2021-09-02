package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.controller.dto.VerificationCodeRequestDto;
import com.cuki.controller.dto.VerifyVerificationCodeRequestDto;
import com.cuki.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    // 이메일 인증 코드 보내기
    @PostMapping("/email")
    public ApiResponse<Boolean> sendVerificationCode(@RequestBody VerificationCodeRequestDto verificationCodeRequestDto) throws Exception {
        emailService.sendVerificationCode(verificationCodeRequestDto);
        return ApiResponse.ok(true);
    }

    // 이메일 인증 코드 검증
    @PostMapping("/verifyCode")
    public ApiResponse<Boolean> verifyCode(@RequestBody VerifyVerificationCodeRequestDto verifyVerificationCodeRequestDto) {
        if(EmailService.verificationCode.equals(verifyVerificationCodeRequestDto.getVerificationCode())) {
            return ApiResponse.ok(true);
        } else {
            return ApiResponse.ok(false);
        }
    }
}
