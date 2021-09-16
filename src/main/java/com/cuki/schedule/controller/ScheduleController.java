package com.cuki.schedule.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.schedule.domain.DateTime;
import com.cuki.schedule.domain.Location;
import com.cuki.schedule.domain.Schedule;
import com.cuki.schedule.dto.*;
import com.cuki.schedule.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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
    public ApiResponse<Page<Schedule>> getScheduleByQueryMethod(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Schedule> all = schedulesService.getAllSchedulesUsingPaging(pageable);
        return ApiResponse.ok(all);
    }

    @PostConstruct
    public void initializing() {
        schedulesService.initializing();
    }

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
