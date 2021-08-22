package com.cuki.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TokenRequestDto {

    private String accessToken;
    @NotBlank
    private String refreshToken;

}
