package io.cuki.domain.schedule.controller;

import io.cuki.domain.participation.dto.ScheduleSummaryResponseDto;
import io.cuki.domain.schedule.service.SchedulesService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.schedule.dto.*;
import io.cuki.global.util.SliceCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;


    @GetMapping
    public ApiResponse<SliceCustom<AllScheduleResponseDto>> getAllSchedule(Pageable pageable) {
        return ApiResponse.ok(schedulesService.getAllSchedule(pageable));
    }

    @PostMapping("/schedules")
    public ApiResponse<IdResponseDto> createSchedule(@RequestBody ScheduleRegistrationRequestDto requestDto) {
        return ApiResponse.ok(schedulesService.createSchedule(requestDto));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ApiResponse<OneScheduleResponseDto> getOneSchedule(@PathVariable Long scheduleId) {
        final OneScheduleResponseDto oneSchedule = schedulesService.getOneSchedule(scheduleId);
        // dto 가 제대로 반환되든지, exception 터져서 ScheduleNotFoundException 이 터지든지
        return ApiResponse.ok(oneSchedule);
    }

    // 일정 요약 정보 조회 (상세 조회 api 에서 detail 만 사용하지 않고 데이터 뿌려주면 안되나?
    @GetMapping("/schedules/{scheduleId}/summary")
    public ApiResponse<ScheduleSummaryResponseDto> getScheduleSummary(@PathVariable Long scheduleId) {
        return ApiResponse.ok(schedulesService.getScheduleSummary(scheduleId));
    }

    @GetMapping("/members/{memberId}/schedules")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule(@PathVariable Long memberId) {
        return ApiResponse.ok(schedulesService.getMySchedule(memberId));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ApiResponse<IdResponseDto> deleteSchedule(@PathVariable Long scheduleId) {
        return ApiResponse.ok(schedulesService.deleteSchedule(scheduleId));
    }

    @PutMapping("/schedules")
    public ApiResponse<IdAndStatusResponseDto> closeUpSchedule(@RequestBody CloseUpScheduleRequestDto closeUpRequestDto) {
        return ApiResponse.ok(schedulesService.closeUpSchedule(closeUpRequestDto));
    }

}
