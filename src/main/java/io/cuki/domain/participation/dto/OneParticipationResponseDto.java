package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.participation.entity.PermissionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class OneParticipationResponseDto {

    private Long participationId;

    private String reasonForParticipation;

    private PermissionResult result;


    public static OneParticipationResponseDto of(Participation participation) {
        return OneParticipationResponseDto.builder()
                    .participationId(participation.getId())
                    .reasonForParticipation(participation.getReasonForParticipation())
                    .result(participation.getResult())
                    .build();
    }


}
