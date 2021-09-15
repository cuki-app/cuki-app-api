package com.cuki.schedule.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.schedule.dto.*;
import com.cuki.schedule.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;


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


    @GetMapping("/schedules/mine")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule() {
        return ApiResponse.ok(schedulesService.getMySchedule());
    }

    @DeleteMapping("/schedules/{id}")
    public ApiResponse<SimpleScheduleResponseDto> deleteSchedule(@PathVariable Long id) {
        return ApiResponse.ok(schedulesService.deleteSchedule(id));
    }

}
