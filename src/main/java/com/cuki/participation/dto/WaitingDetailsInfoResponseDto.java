package com.cuki.participation.dto;

import lombok.Getter;

@Getter
public class WaitingDetailsInfoResponseDto {

    private Long participationId;

    private String nickname;

    private String reasonForParticipation;

    public WaitingDetailsInfoResponseDto(Long participationId, String nickname, String reasonForParticipation) {
        this.participationId = participationId;
        this.nickname = nickname;
        this.reasonForParticipation = reasonForParticipation;
    }
}
