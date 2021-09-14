package com.cuki.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ParticipationSimpleResponseDto {

    private Long scheduleId;

    private Long participationId;
}
