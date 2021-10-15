package io.cuki.domain.schedule.controller;

import io.cuki.domain.schedule.service.SchedulesService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.schedule.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;


    @PostMapping("/schedules")
    public ApiResponse<IdResponseDto> createSchedule(@RequestBody ScheduleRegistrationRequestDto requestDto) {
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


    @GetMapping("/schedules/members/{memberId}")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule(@PathVariable Long memberId) {
        return ApiResponse.ok(schedulesService.getMySchedule(memberId));
    }

    @DeleteMapping("/schedules/{id}")
    public ApiResponse<IdResponseDto> deleteSchedule(@PathVariable Long id) {
        return ApiResponse.ok(schedulesService.deleteSchedule(id));
    }

    @PutMapping("/schedules/status")
    public ApiResponse<IdAndStatusResponseDto> closeUpSchedule(@RequestBody CloseUpScheduleRequestDto closeUpRequestDto) {
        return ApiResponse.ok(schedulesService.closeUpSchedule(closeUpRequestDto));
    }

}
