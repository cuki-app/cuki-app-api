package com.cuki.domain.schedule.dto;

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

    private String place;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private int currentNumberOfPeople;

    private String details;

    private int numberOfPeopleWaiting;

    private ScheduleStatus status;

}
