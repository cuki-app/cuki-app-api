package com.cuki.controller;


import com.cuki.controller.common.ApiResponse;
import com.cuki.controller.dto.ApplyParticipationRequestDto;
import com.cuki.controller.dto.ParticipationSimpleResponseDto;
import com.cuki.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping()
    public ApiResponse<ParticipationSimpleResponseDto> createParticipation(@RequestBody ApplyParticipationRequestDto requestDto) {
        return ApiResponse.ok(participationService.createParticipation(requestDto));
    }
}
