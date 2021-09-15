package com.cuki.controller.dto;

import com.cuki.entity.Member;
import com.cuki.entity.Schedule;
import lombok.Getter;
import java.util.Set;

@Getter
public class ScheduleSummaryAndWaitingListResponseDto {

    private ScheduleSummaryResponseDto scheduleSummary;

    private Set<WaitingInfoResponseDto> members;


    public ScheduleSummaryAndWaitingListResponseDto(ScheduleSummaryResponseDto scheduleSummary, Set<WaitingInfoResponseDto> members) {
        this.scheduleSummary = scheduleSummary;
        this.members = members;
    }
}
