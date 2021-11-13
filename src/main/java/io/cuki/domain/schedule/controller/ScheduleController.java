package io.cuki.domain.schedule.controller;

import io.cuki.domain.schedule.service.SchedulesService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.schedule.dto.*;
import io.cuki.global.util.SliceCustom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@Api(tags = {"모집일정 게시글 API"})
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;


    @ApiOperation(value = "게시글 전체 조회", notes = "모든 게시물을 조회한다.")
    @GetMapping("/schedules")
    public ApiResponse<SliceCustom<AllScheduleResponseDto>> getAllSchedule(@RequestParam int page, @RequestParam(defaultValue = "20") int size) {
        log.debug("ScheduleController 의 getAllSchedule 메소드");
        return ApiResponse.ok(schedulesService.getAllSchedule(page, size));
    }

    @ApiOperation(value = "게시물 등록")
    @PostMapping("/schedules")
    public ApiResponse<IdResponseDto> createSchedule(@RequestBody ScheduleRegistrationRequestDto requestDto) {
        return ApiResponse.ok(schedulesService.createSchedule(requestDto));
    }

    @ApiOperation(value = "게시물 상세조회")
    @GetMapping("/schedules/{scheduleId}")
    public ApiResponse<OneScheduleResponseDto> getOneSchedule(@PathVariable Long scheduleId) {
        final OneScheduleResponseDto oneSchedule = schedulesService.getOneSchedule(scheduleId);
        // dto 가 제대로 반환되든지, exception 터져서 ScheduleNotFoundException 이 터지든지
        return ApiResponse.ok(oneSchedule);
    }


    @ApiOperation(value = "내 게시물 조회")
    @GetMapping("/members/{memberId}/schedules")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule(@PathVariable Long memberId) {
        return ApiResponse.ok(schedulesService.getMySchedule(memberId));
    }

    @ApiOperation(value = "게시물 삭제")
    @DeleteMapping("/schedules/{scheduleId}")
    public ApiResponse<IdResponseDto> deleteSchedule(@PathVariable Long scheduleId) {
        return ApiResponse.ok(schedulesService.deleteSchedule(scheduleId));
    }

    @ApiOperation(value = "게시물 모집 상태 - 마감으로 변경")
    @PutMapping("/schedules/{scheduleId}/status")
    public ApiResponse<IdAndStatusResponseDto> closeUpSchedule(@RequestBody CloseUpScheduleRequestDto closeUpRequestDto) {
        return ApiResponse.ok(schedulesService.closeUpSchedule(closeUpRequestDto));
    }

}
