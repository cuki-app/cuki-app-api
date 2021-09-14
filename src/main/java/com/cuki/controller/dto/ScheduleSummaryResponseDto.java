package com.cuki.controller.dto;

import com.cuki.entity.Location;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ScheduleSummaryResponseDto {

    private Long scheduleId;

    private String title;

    private String place;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private int currentNumberOfPeople;

    private int numberOfPeopleWaiting;
}
