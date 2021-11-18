package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ParticipantInfoResponseDto {
    private Long participationId;
    private String nickname;

    public static ParticipantInfoResponseDto of(Participation participation) {
        return new ParticipantInfoResponseDto(participation.getId(), participation.getMember().getNickname().getNickname());
    }
}
