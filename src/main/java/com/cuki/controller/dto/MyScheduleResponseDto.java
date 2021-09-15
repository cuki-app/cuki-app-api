package com.cuki.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class MyScheduleResponseDto implements Comparable<MyScheduleResponseDto> {

    private Long scheduleId;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;


    @Override
    public int compareTo(MyScheduleResponseDto o) {

        final LocalDateTime now = LocalDateTime.now();  // 2021-08-22
        final long dDays = Duration.between(now, getStartDateTime()).toDays();
        final long targetDdays = Duration.between(now, o.getStartDateTime()).toDays();

        return Long.compare(dDays, targetDdays);
    }
}
