package com.cuki.domain.member.controller;

import com.cuki.global.common.response.ApiResponse;
import com.cuki.domain.member.dto.MemberInfoForAdminResponseDto;
import com.cuki.domain.member.dto.MemberInfoResponseDto;
import com.cuki.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<MemberInfoResponseDto> getMyInfo() {
        return ApiResponse.ok(memberService.getMyInfo());
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<MemberInfoForAdminResponseDto> getMemberInfo(@PathVariable String email) {
        return ApiResponse.ok(memberService.getMemberInfo(email));
    }
}
