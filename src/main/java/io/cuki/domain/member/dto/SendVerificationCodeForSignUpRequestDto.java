package io.cuki.domain.member.dto;

import lombok.Getter;

@Getter
public class SendVerificationCodeForSignUpRequestDto {
    private String email;
}
