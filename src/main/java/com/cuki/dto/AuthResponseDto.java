package com.cuki.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;

}
