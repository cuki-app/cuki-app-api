package io.cuki.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllScheduleWithSliceResponseDto {  // temporary

    private List<AllScheduleResponseDto> contents;

    private boolean sliceHasNext;

    public static AllScheduleWithSliceResponseDto of(List<AllScheduleResponseDto> contents, boolean sliceHasNext) {
        return new AllScheduleWithSliceResponseDto(contents, sliceHasNext);
    }

}
