package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingListInfoResponseDto {

    private Long participationId;

    private String nickname;

    private String reasonForParticipation;

    public static WaitingListInfoResponseDto of(Participation participation) {
        return new WaitingListInfoResponseDto(participation.getId(), participation.getMember().getNickname().getNickname(), participation.getReasonForParticipation());
    }

}
