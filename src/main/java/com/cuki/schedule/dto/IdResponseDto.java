package com.cuki.schedule.dto;

import lombok.Getter;

@Getter
public class IdResponseDto {

    private Long scheduleId;

    public IdResponseDto(Long id) {
        this.scheduleId = id;
    }
}
