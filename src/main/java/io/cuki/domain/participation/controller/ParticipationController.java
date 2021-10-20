package io.cuki.domain.participation.controller;


import io.cuki.domain.participation.dto.*;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.participation.service.ParticipationService;
import io.cuki.global.common.response.ErrorResponse;
import io.cuki.global.error.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;


@RequiredArgsConstructor
@RestController
public class ParticipationController {

    private final ParticipationService participationService;


    @PostMapping("/schedules/participation")
    public ApiResponse<ParticipationSimpleResponseDto> createParticipation(@RequestBody ApplyParticipationRequestDto requestDto) throws IllegalAccessException {
        // try-catch 로 ErrorResponse 응답 할 때 제네릭 타입 반환형이 ok 응답 반환형과 맞지 않는 상황에 어떻게 할 것인가?
        return ApiResponse.ok(participationService.createParticipation(requestDto));
    }

    @PostMapping("/schedules/participation/test")
    public ResponseEntity createParticipationTest(@RequestBody ApplyParticipationRequestDto requestDto) throws IllegalAccessException {
        // try-catch 로 ErrorResponse 응답 할 때 제네릭 타입 반환형이 ok 응답 반환형과 맞지 않는 상황에 어떻게 할 것인가?
        try {
            return new ResponseEntity(ApiResponse.ok(participationService.createParticipation(requestDto)), HttpStatus.OK);

        } catch (IllegalAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.badRequest(new MessageResponseDto(e.getMessage())));
        }

    }

    @GetMapping("/schedules/{scheduleId}/participants/permission/none")
    public ApiResponse<Set<WaitingInfoResponseDto>> getWaitingList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getWaitingList(scheduleId));
    }

    // scheduleId 가 들어가야 rest 스럽지만, 변수가 필요가 없을 때는?
    @GetMapping("/schedules/participants/{participantId}")
    public ApiResponse<WaitingDetailsInfoResponseDto> getWaitingDetailsInfo(@PathVariable Long participantId) throws IllegalAccessException {
        return ApiResponse.ok(participationService.getWaitingDetailsInfo(participantId));
    }

    @PutMapping("/schedules/participants/permission")
    public ApiResponse<PermissionResponseDto> updatePermission(@RequestBody PermissionRequestDto permissionRequestDto) throws IllegalAccessException {
        return ApiResponse.ok(participationService.updatePermission(permissionRequestDto));
    }

    @GetMapping("/schedules/{scheduleId}/participants/permission/accept")
    public ApiResponse<Set<ParticipantInfoResponseDto>> getParticipantList(@PathVariable Long scheduleId) {
        return ApiResponse.ok(participationService.getParticipantList(scheduleId));
    }
}
