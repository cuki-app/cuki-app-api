package com.cuki.domain.participation.dto;

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
