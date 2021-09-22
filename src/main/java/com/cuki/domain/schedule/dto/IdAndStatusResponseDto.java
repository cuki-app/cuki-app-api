package com.cuki.domain.schedule.dto;

import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdAndStatusResponseDto {
    private Long scheduldId;

    private ScheduleStatus status;

    public static IdAndStatusResponseDto of(Schedule schedule) {
        return new IdAndStatusResponseDto(schedule.getId(), schedule.getStatus());
    }
}
