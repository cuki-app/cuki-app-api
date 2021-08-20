package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.ScheduleRegistrationRequestDto;
import com.cuki.entity.Schedule;
import com.cuki.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 1. 모집글 등록
 * 2. 모집글 조회
 * 2.1 일정 메인 - 내 일정, 지금 막 들어온 일정
 * 2.2 특정 모집글 조회
 * 3. 모집글 삭제
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final SchedulesService schedulesService;

    @PostMapping
    public ApiResponse<Long> save(@RequestBody ScheduleRegistrationRequestDto requestDto) {
        return schedulesService.save(requestDto);
    }

    @GetMapping
    public ApiResponse<List<Schedule>> readAll() {
        return schedulesService.findAll();
    }

}
