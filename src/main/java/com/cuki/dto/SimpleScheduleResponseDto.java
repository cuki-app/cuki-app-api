package com.cuki.dto;

import lombok.Getter;

@Getter
public class SimpleScheduleResponseDto {

    Long scheduleId;

    public SimpleScheduleResponseDto(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
