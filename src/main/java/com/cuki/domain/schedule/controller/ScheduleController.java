package com.cuki.domain.schedule.controller;

import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.service.SchedulesService;
import com.cuki.global.common.response.ApiResponse;
import com.cuki.domain.schedule.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;

    // 페이징
    @GetMapping("/schedules/page")
    public ApiResponse<Page<Schedule>> getAllSchedulesUsingPaging() {
        Pageable pageable = PageRequest.of(0, 8);
        Page<Schedule> all = schedulesService.getAllSchedulesUsingPaging(pageable);
        return ApiResponse.ok(all);
    }

    // 쿼리 메서드 사용
    @GetMapping("/schedules/page/queryMethod")
    public ApiResponse<Page<Schedule>> getScheduleByQueryMethod() {
        Pageable pageable = PageRequest.of(0, 3);
        final Page<Schedule> schedules = schedulesService.getScheduleByQueryMethod(pageable);
        return ApiResponse.ok(schedules);
    }

    // 쿼리 파라미터로 정보 받기
    @GetMapping("/schedules/page/queryParameter")
    public ApiResponse<Page<Schedule>> getScheduleByQueryParameter(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Schedule> all = schedulesService.getAllSchedulesUsingPaging(pageable);
        return ApiResponse.ok(all);
    }

    // spring web mvc 로 더 간단하게
    @GetMapping("/schedules/page/mvc")
    public ApiResponse<Page<Schedule>> getScheduleByQueryMethod(Pageable pageable) {
        Page<Schedule> all = schedulesService.getAllSchedulesUsingPaging(pageable);
        return ApiResponse.ok(all);
    }

    @PostConstruct
    public void initializing() {
        schedulesService.initializing();
    }

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
    public ApiResponse<IdAndStatusResponseDto> updateStatus(@RequestBody UpdateStatusRequestDto statusRequestDto) {
        return ApiResponse.ok(schedulesService.updateStatus(statusRequestDto));
    }

}
