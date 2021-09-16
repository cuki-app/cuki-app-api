package com.cuki.controller;

import com.cuki.controller.common.ApiResponse;
import com.cuki.controller.dto.MemberInfoForAdminResponseDto;
import com.cuki.controller.dto.SignUpRequestDto;
import com.cuki.controller.dto.MemberInfoResponseDto;
import com.cuki.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
