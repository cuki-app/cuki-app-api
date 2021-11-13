package io.cuki.domain.member.service;

import io.cuki.domain.member.dto.MemberInfoResponseDto;
import io.cuki.domain.member.dto.UpdateMyPageInfoRequestDto;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.global.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberInfoResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberInfoResponseDto::of)
                .orElseThrow(MemberNotFoundException::new);

    }

    public MemberInfoResponseDto getMyPageInfo(Long memberId) {
        if (!Objects.equals(memberId, SecurityUtil.getCurrentMemberId())) {
            throw new MemberNotMatchException("현재 로그인한 회원의 정보와 시큐리티 컨텍스트에 저장되어 있는 회원 정보가 일치하지 않습니다.");
        }
        return memberRepository.findById(memberId)
                .map(MemberInfoResponseDto::of)
                .orElseThrow(MemberNotFoundException::new);
    }

    public MemberInfoResponseDto updateMyPageInfo(Long memberId, UpdateMyPageInfoRequestDto requestDto) {
        if (!Objects.equals(memberId, SecurityUtil.getCurrentMemberId())) {
            throw new MemberNotMatchException("현재 로그인한 회원의 정보와 시큐리티 컨텍스트에 저장되어 있는 회원 정보가 일치하지 않습니다.");
        }
        return memberRepository.findById(memberId)
                .map(member -> member.updateMemberInfo(requestDto))
                .orElseThrow(MemberNotFoundException::new);
    }
}
