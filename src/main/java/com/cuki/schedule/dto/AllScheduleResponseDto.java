package com.cuki.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class AllScheduleResponseDto {

    private final Long scheduleId;

    private String title;

    private String place;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")     // test
    private LocalDateTime startDateTime;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;    // 3명

    private int currentNumberOfPeople;  // 1명 -> 2명 -> 3명

}
