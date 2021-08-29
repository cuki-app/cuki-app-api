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

    @GetMapping("/")
    public ApiResponse<MainScheduleResponseDto> getMainSchedule() {
        return ApiResponse.ok(schedulesService.getMainSchedule());
        // {
        //    "timestamp": "2021-08-26T15:03:28.854+00:00",
        //    "status": 404,
        //    "error": "Not Found",
        //    "path": "/schedule"
        //}
    }

    @GetMapping("/schedules")
    public ApiResponse<List<AllScheduleResponseDto>> getAllSchedule() {
        return ApiResponse.ok(schedulesService.getAllSchedule());
    }

    @GetMapping("/schedules/mine")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule() {
        return ApiResponse.ok(schedulesService.getMySchedule());
    }

    @GetMapping("/schedules/{id}")
    public ApiResponse<OneScheduleResponseDto> getOneSchedule(@PathVariable Long id) {
        final OneScheduleResponseDto oneSchedule = schedulesService.getOneSchedule(id);
        return ApiResponse.ok(oneSchedule);
    }

    @PostMapping("/schedules")
    public ApiResponse<SimpleScheduleResponseDto> createSchedule(@RequestBody ScheduleRegistrationRequestDto requestDto) {
        final SimpleScheduleResponseDto responseDto = schedulesService.createSchedule(requestDto);
        return ApiResponse.ok(responseDto);
    }

    @DeleteMapping("/schedules/{id}")
    public ApiResponse<SimpleScheduleResponseDto> deleteSchedule(@PathVariable Long id) {
        return ApiResponse.ok(schedulesService.deleteSchedule(id));
    }
}
