package io.cuki.domain.participation.dto;

import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class MyParticipationResponseDto implements Comparable<MyParticipationResponseDto> {

    private Long scheduleId;

    private Long participationId;

    private String title;

    private String nickname;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private ScheduleStatus status;

    public static MyParticipationResponseDto of(Participation participation) {
        return MyParticipationResponseDto.builder()
                .scheduleId(participation.getSchedule().getId())
                .participationId(participation.getId())
                .title(participation.getSchedule().getTitle())
                .nickname(participation.getSchedule().getMember().getNickname().getNickname())
                .startDateTime(participation.getSchedule().getDateTime().getStartDateTime())
                .endDateTime(participation.getSchedule().getDateTime().getEndDateTime())
                .status(participation.getSchedule().getStatus())
                .build();
    }


    @Override
    public int compareTo(MyParticipationResponseDto o) {
        final LocalDateTime now = LocalDateTime.now();
        final long dDays = Duration.between(now, getStartDateTime()).toDays();
        final long targetDdays = Duration.between(now, o.getStartDateTime()).toDays();

        return Long.compare(dDays, targetDdays);
    }
}
