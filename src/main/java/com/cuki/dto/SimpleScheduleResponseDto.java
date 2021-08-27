package com.cuki.dto;

import lombok.Getter;

@Getter
public class SimpleScheduleResponseDto {

    Long id;

    public SimpleScheduleResponseDto(Long id) {
        this.id = id;
    }
}
