package io.cuki.domain.participation.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ApplyParticipationRequestDto {

    private Long scheduleId;

    private String reasonForParticipation;
}
