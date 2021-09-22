package com.cuki.domain.participation.controller;


import com.cuki.global.common.response.ApiResponse;
import com.cuki.domain.participation.dto.*;
import com.cuki.domain.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RequiredArgsConstructor
@RestController
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    @GetMapping("/summary/{scheduleId}")
    public ApiResponse<ScheduleSummaryResponseDto> getScheduleSummary(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getScheduleSummary(scheduleId));
    }

    @PostMapping()
    public ApiResponse<ParticipationSimpleResponseDto> createParticipation(@RequestBody ApplyParticipationRequestDto requestDto) throws IllegalAccessException {
        return ApiResponse.ok(participationService.createParticipation(requestDto));
    }

    @GetMapping("/list/check/none/{scheduleId}")
    public ApiResponse<Set<WaitingInfoResponseDto>> getWaitingList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getWaitingList(scheduleId));
    }

    @GetMapping("/info/{participationId}")
    public ApiResponse<WaitingDetailsInfoResponseDto> getWaitingDetailsInfo(@PathVariable Long participationId) throws IllegalAccessException {
        return ApiResponse.ok(participationService.getWaitingDetailsInfo(participationId));
    }

    @PutMapping("/permission")
    public ApiResponse<PermissionResponseDto> updatePermission(@RequestBody PermissionRequestDto permissionRequestDto) throws IllegalAccessException {
        return ApiResponse.ok(participationService.updatePermission(permissionRequestDto));
    }

    @GetMapping("/list/check/{scheduleId}")
    public ApiResponse<Set<ParticipantInfoResponseDto>> getParticipantList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getParticipantList(scheduleId));
    }
}
