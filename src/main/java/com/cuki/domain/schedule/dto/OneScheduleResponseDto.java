package com.cuki.domain.schedule.dto;

import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@Getter
public class OneScheduleResponseDto {

    private Long scheduleId;

    private String title;

    private String nickname;

    private String place;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private int currentNumberOfPeople;

    private String details;

    private int numberOfPeopleWaiting;

    private ScheduleStatus status;

    public static OneScheduleResponseDto of(Schedule schedule) {
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
                .build();
    }

}
