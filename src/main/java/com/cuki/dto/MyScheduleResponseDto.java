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

    // 로직 다시 짜야한다.
    @Override
    public int compareTo(MyScheduleResponseDto o) {
        // local date 끼리만 연산
        Duration duration = Duration.between(startDateTime, endDateTime);
        log.info("seconds = {}", duration.getSeconds());
        log.info("nano seconds = {}", duration.getNano());

        return duration.getNano();

    }
}
