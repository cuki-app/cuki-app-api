package io.cuki.domain.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ParticipationSimpleResponseDto {

    private Long scheduleId;

    private Long participationId;
}
