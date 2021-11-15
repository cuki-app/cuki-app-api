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

    @ApiOperation(value = "참여 요청하기")
    @PostMapping("/schedules/{scheduleId}/participation")
    public ApiResponse<ParticipationSimpleResponseDto> createParticipation(@RequestBody ApplyParticipationRequestDto requestDto) throws IllegalAccessException {
        log.debug("참여 요청 = {}", requestDto);
        // try-catch 로 ErrorResponse 응답 할 때 제네릭 타입 반환형이 ok 응답 반환형과 맞지 않는 상황에 어떻게 할 것인가?
        return ApiResponse.ok(participationService.createParticipation(requestDto));
    }

    @ApiOperation(value = "내가 참여한 게시물 전체 조회")
    @GetMapping("/members/{memberId}/participation")
    public ApiResponse<List<ParticipationResponseDto>> getMyParticipation(@PathVariable Long memberId) {
        if (SecurityUtil.getCurrentMemberId().equals(memberId)) {
            return ApiResponse.ok(participationService.getMyParticipation(memberId));
        }

        throw new MemberNotMatchException("파라미터 member id: " + memberId + " 와 로그인 한 유저의 member id:"+ SecurityUtil.getCurrentMemberId()+ " 가 일치 하지 않습니다.");
    }

    @ApiOperation(value = "내가 참여한 게시물 상세 조회", notes = "게시글 정보, 내 신청 정보와 결과 보여주기")
    @GetMapping("/members/{memberId}/participation/{participationId}")
    public ApiResponse<OneParticipationResponseDto> getOneParticipation(@PathVariable Long memberId, @PathVariable Long participationId) {
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new MemberNotMatchException("파라미터 member id: " + memberId + " 와 로그인 한 유저의 member id:"+ SecurityUtil.getCurrentMemberId()+ " 가 일치 하지 않습니다.");
        }
        return ApiResponse.ok(participationService.getOneParticipation(participationId));
    }

    @ApiOperation(value = "참여 요청한 대기자 리스트 조회", notes = "대기자에 대한 정보 - nickname 보여주기, 아래 API만 살리면 어떨까?")
    @GetMapping("/schedules/{scheduleId}/participants/permission/none")
    public ApiResponse<Set<WaitingInfoResponseDto>> getWaitingList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getWaitingList(scheduleId));
    }

    @ApiOperation(value = "'참여' 요청한 대기자의 정보 조회", notes = "대기자에 대한 정보 - nickname, 참여 이유")
    @GetMapping("/schedules/{scheduleId}/participants/{participantId}")
    public ApiResponse<WaitingDetailsInfoResponseDto> getWaitingDetailsInfo(@PathVariable Long participantId) throws IllegalAccessException {
        return ApiResponse.ok(participationService.getWaitingDetailsInfo(participantId));
    }

    @ApiOperation(value = "참여 수락 또는 거절하기")
    @PutMapping("/schedules/{scheduleId}/participants/permission")
    public ApiResponse<PermissionResponseDto> updatePermission(@RequestBody PermissionRequestDto permissionRequestDto) throws IllegalAccessException {
        log.debug("참여 수락 또는 거절 = {}", permissionRequestDto);
        return ApiResponse.ok(participationService.updatePermission(permissionRequestDto));
    }

    @ApiOperation(value = "참여 확정자 리스트 조회")
    @GetMapping("/schedules/{scheduleId}/participants")
    public ApiResponse<Set<ParticipantInfoResponseDto>> getParticipantList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getParticipantList(scheduleId));
    }
}
