package io.cuki.domain.schedule.controller;

import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.schedule.exception.ScheduleStatusIsAlreadyChangedException;
import io.cuki.domain.schedule.service.SchedulesService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.schedule.dto.*;
import io.cuki.global.util.SecurityUtil;
import io.cuki.global.util.SliceCustom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@Api(tags = {"게시글(Schedule) API"})
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulesService schedulesService;


    @ApiOperation(value = "게시글 등록")
    @PostMapping("/schedules")
    public ApiResponse<IdResponseDto> createSchedule(@RequestBody ScheduleRegistrationRequestDto requestDto) {
        log.debug("registrationRequestDto = {}", requestDto);
        return ApiResponse.ok(schedulesService.createSchedule(requestDto));
    }

    @ApiOperation(value = "게시글 전체 조회", notes = "모든 게시글을 조회한다.")
    @GetMapping("/schedules")
    public ApiResponse<SliceCustom<AllScheduleResponseDto>> getAllSchedule(@RequestParam int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(schedulesService.getAllSchedule(page, size));
    }

    @ApiOperation(value = "게시글 상세조회")
    @GetMapping("/schedules/{scheduleId}")
    public ApiResponse<OneScheduleResponseDto> getOneSchedule(@PathVariable Long scheduleId) {
        final OneScheduleResponseDto oneSchedule = schedulesService.getOneSchedule(scheduleId);
        return ApiResponse.ok(oneSchedule);
    }


    @ApiOperation(value = "내가 등록한 게시글 전체 조회")
    @GetMapping("/members/{memberId}/schedules")
    public ApiResponse<List<MyScheduleResponseDto>> getMySchedule(@PathVariable Long memberId) {
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new MemberNotMatchException("현재 로그인 한 회원과 게시글 작성자 회원 정보가 일치하지 않습니다.");
        }

        return ApiResponse.ok(schedulesService.getMySchedule(memberId));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/schedules/{scheduleId}")
    public ApiResponse<IdResponseDto> deleteSchedule(@PathVariable Long scheduleId) {
        return ApiResponse.ok(schedulesService.deleteSchedule(scheduleId));
    }

    @ApiOperation(value = "게시글 상태 - 모집중 -> 마감(DONE)으로 변경")
    @PutMapping("/schedules/{scheduleId}/status")
    public ApiResponse<IdAndStatusResponseDto> changeScheduleStatus(@PathVariable Long scheduleId) throws ScheduleStatusIsAlreadyChangedException {
        return ApiResponse.ok(schedulesService.changeScheduleStatus(scheduleId));
    }

    @ApiOperation(value = "슬랙 어펜더 테스트용 API")
    @GetMapping("/slack")
    public void slackAppender() {
        log.error("슬랙 어펜더 테스트용 API 입니다. error log.");
    }
}
