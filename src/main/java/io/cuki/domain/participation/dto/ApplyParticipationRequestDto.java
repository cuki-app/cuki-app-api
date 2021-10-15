package io.cuki.domain.participation.dto;

import lombok.Getter;

@Getter
public class ApplyParticipationRequestDto {

    private Long scheduleId;

    private String reasonForParticipation;
}
