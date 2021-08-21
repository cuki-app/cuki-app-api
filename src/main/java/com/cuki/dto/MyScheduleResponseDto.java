package com.cuki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class MyScheduleResponseDto implements Comparable<MyScheduleResponseDto> {

    private Long id;

    private String title;

    private boolean allDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    private int participants;

    private String place;

    /**
     * 1. start date 기준으로 정렬
     * 2. start date ~ end date 차이로 정렬
     */
    @Override
    public int compareTo(MyScheduleResponseDto o) {

        final LocalDateTime now = LocalDateTime.now();  // 2021-08-22
        final long days = Duration.between(now, startDateTime).toDays();
        final long targetDays = Duration.between(now, o.getStartDateTime()).toDays();

        return Long.compare(days, targetDays);
    }
}
