package com.cuki.controller.dto;

import com.cuki.entity.Purpose;
import lombok.Getter;

@Getter
public class VerificationCodeRequestDto {

    private String email;
    private Purpose purpose;

}
