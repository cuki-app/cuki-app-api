package io.cuki.domain.schedule.dto;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class AllScheduleResponseDto {

    private final Long scheduleId;

    private final String title;

    private final String nickname;

    private final String place;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final int fixedNumberOfPeople;

    private final int currentNumberOfPeople;

    private final ScheduleStatus status;

    public static AllScheduleResponseDto of(Schedule schedule) {
        return AllScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .nickname(schedule.getMember().getNickname())
                .place(schedule.getLocation().getPlace())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                .status(schedule.getStatus())
                .build();
    }

}
