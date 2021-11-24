package io.cuki.domain.participation.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.participation.entity.PermissionResult;
import io.cuki.domain.schedule.entity.Schedule;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ParticipationRegistrationRequestDto {

    private Long memberId;
    private String reasonForParticipation;


    public Participation toEntity(Member member, Schedule schedule) {

        return Participation.builder()
                    .member(member)
                    .schedule(schedule)
                    .reasonForParticipation(reasonForParticipation)
                    .build();
    }
}
