package com.cuki.schedule.dto;

import lombok.Getter;

@Getter
public class SimpleScheduleResponseDto {

    private Long scheduleId;

    public SimpleScheduleResponseDto(Long id) {
        this.scheduleId = id;
    }
}