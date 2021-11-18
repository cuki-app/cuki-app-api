package io.cuki.domain.member.controller;

import io.cuki.domain.member.dto.UpdateMyPageInfoRequestDto;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.member.service.MemberService;
import io.cuki.global.common.response.ApiResponse;
import io.cuki.domain.member.dto.MemberInfoResponseDto;
import io.cuki.global.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        if (!Objects.equals(memberId, SecurityUtil.getCurrentMemberId())) {
            throw new MemberNotMatchException("현재 로그인한 회원의 정보와 시큐리티 컨텍스트에 저장되어 있는 회원 정보가 일치하지 않습니다.");
        }
        return ApiResponse.ok(memberService.getMyPageInfo(memberId));
    }

    @ApiOperation(value = "마이페이지 수정")
    @PutMapping("/{memberId}")
    public ApiResponse<MemberInfoResponseDto> updateMyPageInfo(@PathVariable Long memberId, @RequestBody UpdateMyPageInfoRequestDto requestDto) {
        if (!Objects.equals(memberId, SecurityUtil.getCurrentMemberId())) {
            throw new MemberNotMatchException("현재 로그인한 회원의 정보와 시큐리티 컨텍스트에 저장되어 있는 회원 정보가 일치하지 않습니다.");
        }
        return ApiResponse.ok(memberService.updateMyPageInfo(memberId, requestDto));
    }
}
