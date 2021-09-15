package com.cuki.controller.dto;

import lombok.Getter;

@Getter
public class WaitingInfoResponseDto {

    private Long participationId;

    private String nickname;

    public WaitingInfoResponseDto(Long participationId, String nickname) {
        this.participationId = participationId;
        this.nickname = nickname;
    }
}
