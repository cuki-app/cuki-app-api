package com.cuki.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

}
