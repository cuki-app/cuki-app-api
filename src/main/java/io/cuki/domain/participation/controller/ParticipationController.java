package io.cuki.domain.participation.controller;


import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.participation.dto.*;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.participation.service.ParticipationService;
import io.cuki.global.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;


@Api(tags = {"모집일정 게시글에 참여하기 API"})
@Slf4j
@RequiredArgsConstructor
@RestController
public class ParticipationController {

    private final ParticipationService participationService;


    @ApiOperation(value = "참여 요청하기", notes = "guest 가 요청")
    @PostMapping("/schedules/{scheduleId}/participation")
    public ApiResponse<ParticipationResultResponseDto> createParticipation(@PathVariable Long scheduleId, @RequestBody ParticipationRegistrationRequestDto requestDto) {
        log.debug("참여 요청 = {}", requestDto);
        if (!SecurityUtil.getCurrentMemberId().equals(requestDto.getMemberId())) {
            throw new MemberNotMatchException("현재 로그인 한 회원과 참여 요청한 회원 정보가 일치하지 않습니다.");
        }

        return ApiResponse.ok(participationService.createParticipation(scheduleId, requestDto));
    }


    @ApiOperation(value = "내가 참여한 게시물 전체 조회", notes = "참여 당사자만 조회 가능")
    @GetMapping("/members/{memberId}/participation")
    public ApiResponse<List<MyParticipationResponseDto>> getMyParticipationList(@PathVariable Long memberId) {
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new MemberNotMatchException("파라미터 member id: " + memberId + " 와 로그인 한 회원의 member id:"+ SecurityUtil.getCurrentMemberId()+ " 가 일치 하지 않습니다.");
        }
        return ApiResponse.ok(participationService.getMyParticipationList(memberId));
    }


    @ApiOperation(value = "내가 참여한 게시물 상세 조회", notes = "참여 정보에 관한 데이터 response, 참여 당사자만 조회 가능")
    @GetMapping("/members/{memberId}/participation/{participationId}")
    public ApiResponse<OneParticipationResponseDto> getOneParticipation(@PathVariable Long memberId, @PathVariable Long participationId) {
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new MemberNotMatchException("파라미터 member id: " + memberId + " 와 로그인 한 회원의 member id:"+ SecurityUtil.getCurrentMemberId()+ " 가 일치 하지 않습니다.");
        }
        return ApiResponse.ok(participationService.getOneParticipation(memberId, participationId));
    }


    @ApiOperation(value = "참여 수락 또는 거절하기", notes = "게시글 작성자만 조회 가능" )
    @PutMapping("/schedules/{scheduleId}/participants/permission")
    public ApiResponse<PermissionResponseDto> updatePermission(@RequestBody PermissionRequestDto permissionRequestDto) {
        log.debug("참여 수락 또는 거절 = {}", permissionRequestDto);
        return ApiResponse.ok(participationService.updatePermission(permissionRequestDto));
    }


    @ApiOperation(value = "참여 결정을 기다리는 대기자들 명단 조회", notes = "게시글 작성자만 조회 가능")
    @GetMapping("/schedules/{scheduleId}/participants/permission/none")
    public ApiResponse<Set<WaitingListInfoResponseDto>> getWaitingList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getWaitingList(scheduleId));
    }


    @ApiOperation(value = "참여 확정자 리스트(닉네임) 명단 조회", notes = "모든 회원이 조회 가능")
    @GetMapping("/schedules/{scheduleId}/participants")
    public ApiResponse<Set<ParticipantInfoResponseDto>> getParticipantList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getParticipantList(scheduleId));
    }

}
