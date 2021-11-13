package io.cuki.domain.member.controller;

import io.cuki.domain.member.dto.UpdateMyPageInfoRequestDto;
import io.cuki.domain.member.service.MemberService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.member.dto.MemberInfoResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원 관련 API"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "내 정보 조회")
    @GetMapping("/me")
    public ApiResponse<MemberInfoResponseDto> getMyInfo() {
        return ApiResponse.ok(memberService.getMyInfo());
    }

    @ApiOperation(value = "마이페이지 조회")
    @GetMapping("/{memberId}")
    public ApiResponse<MemberInfoResponseDto> getMyPageInfo(@PathVariable Long memberId) {
        return ApiResponse.ok(memberService.getMyPageInfo(memberId));
    }

    @ApiOperation(value = "마이페이지 수정")
    @PutMapping("/{memberId}")
    public ApiResponse<MemberInfoResponseDto> updateMyPageInfo(@PathVariable Long memberId, @RequestBody UpdateMyPageInfoRequestDto requestDto) {
        return ApiResponse.ok(memberService.updateMyPageInfo(memberId, requestDto));
    }
}
