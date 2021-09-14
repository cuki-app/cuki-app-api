package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.controller.dto.*;
import com.cuki.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;

//    @GetMapping("/")
//    public ApiResponse<MainScheduleResponseDto> getMainSchedule() {
//        return ApiResponse.ok(schedulesService.getMainSchedule());
//    }

    @PostMapping("/schedules")
    public ApiResponse<SimpleScheduleResponseDto> createSchedule(@RequestBody ScheduleRegistrationRequestDto requestDto) {
        return ApiResponse.ok(schedulesService.createSchedule(requestDto));
    }

    @GetMapping("/")
    public ApiResponse<List<AllScheduleResponseDto>> getAllSchedule() {
        return ApiResponse.ok(schedulesService.getAllSchedule());
    }

    @GetMapping("/schedules/{scheduleId}")
    public ApiResponse<OneScheduleResponseDto> getOneSchedule(@PathVariable Long scheduleId) {
        final OneScheduleResponseDto oneSchedule = schedulesService.getOneSchedule(scheduleId);
        return ApiResponse.ok(oneSchedule);
    }

    @GetMapping("/schedules/summary/{scheduleId}")
    public ApiResponse<ScheduleSummaryResponseDto> getScheduleSummary(@PathVariable Long scheduleId) {
        return ApiResponse.ok(schedulesService.getScheduleSummary(scheduleId));
    }

    @GetMapping("/schedules/mine")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule() {
        return ApiResponse.ok(schedulesService.getMySchedule());
    }

    @DeleteMapping("/schedules/{id}")
    public ApiResponse<SimpleScheduleResponseDto> deleteSchedule(@PathVariable Long id) {
        return ApiResponse.ok(schedulesService.deleteSchedule(id));
    }

    @GetMapping("/schedules/participation/{scheduleId}")
    public ApiResponse<SimpleScheduleResponseDto> joinSchedule(@PathVariable Long scheduleId) throws IllegalAccessException {
        return ApiResponse.ok(schedulesService.joinSchedule(scheduleId));
    }
}
