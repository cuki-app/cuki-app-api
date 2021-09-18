package com.cuki.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

// InvalidDefinitionException
@Getter
@Builder
@AllArgsConstructor
public class MainScheduleResponseDto {

    private List<MyScheduleResponseDto> mySchedule;

    private List<AllScheduleResponseDto> allSchedule;

}
