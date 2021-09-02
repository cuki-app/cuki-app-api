package com.cuki.controller.dto;

import lombok.Getter;

@Getter
public class VerifyVerificationCodeRequestDto {
    private String email;
    private String verificationCode;

}
