package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingInfoResponseDto {

    private Long participationId;

    private String nickname;

    public static WaitingInfoResponseDto of(Participation participation) {
        return new WaitingInfoResponseDto(participation.getId(), participation.getMember().getNickname().getNickname());
    }

}
