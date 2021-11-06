package io.cuki.domain.member.controller;

import io.cuki.domain.member.service.MemberService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.member.dto.MemberInfoForAdminResponseDto;
import io.cuki.domain.member.dto.MemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponseDto> getMyInfo() {
        return ApiResponse.ok(memberService.getMyInfo());
    }
}
