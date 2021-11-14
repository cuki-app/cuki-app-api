package io.cuki.domain.schedule.dto;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleAuthority;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class OneScheduleResponseDto {

    private final Long scheduleId;

    private final String title;

    private final String nickname;

    private final String place;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final int fixedNumberOfPeople;

    private final int currentNumberOfPeople;

    private final String details;

    private final int numberOfPeopleWaiting;

    private final ScheduleStatus status;

    private final ScheduleAuthority owner;

    public static OneScheduleResponseDto of(Schedule schedule, ScheduleAuthority owner) {
        return OneScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .nickname(schedule.getMember().getNickname())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                .place(schedule.getLocation().getPlace())
                .details(schedule.getDetails())
                .numberOfPeopleWaiting(schedule.getNumberOfPeopleWaiting())
                .status(schedule.getStatus())
                .owner(owner)
                .build();
    }

}
