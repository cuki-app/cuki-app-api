package com.cuki.participation.controller;


import com.cuki.controller.common.ApiResponse;
import com.cuki.participation.dto.*;
import com.cuki.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/check/{scheduleId}")
    public ApiResponse<ScheduleSummaryAndWaitingListResponseDto> getWaitingList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getWaitingList(scheduleId));
    }

    @GetMapping("/info/{participationId}")
    public ApiResponse<WaitingDetailsInfoResponseDto> getWaitingDetailsInfo(@PathVariable Long participationId) throws IllegalAccessException {
        return ApiResponse.ok(participationService.getWaitingDetailsInfo(participationId));
    }

    @PostMapping("/permission")
    public ApiResponse<PermissionResponseDto> decidePermission(@RequestBody PermissionRequestDto permissionRequestDto) {
        return ApiResponse.ok(participationService.decidePermission(permissionRequestDto));
    }
}
