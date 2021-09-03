package com.cuki.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@Getter
public class OneScheduleResponseDto {

    private Long id;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private int currentNumberOfPeople;

    private String place;

    private String description;


}
