package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ParticipationResultResponseDto {

    private Long scheduleId;

    private Long participationId;

    public static ParticipationResultResponseDto of(Participation participation) {
        return new ParticipationResultResponseDto(participation.getSchedule().getId(), participation.getId());
    }

}
