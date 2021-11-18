package io.cuki.domain.member.service;

import io.cuki.domain.member.dto.MemberInfoResponseDto;
import io.cuki.domain.member.dto.UpdateMyPageInfoRequestDto;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.global.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return memberRepository.findById(memberId)
                .map(MemberInfoResponseDto::of)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public MemberInfoResponseDto updateMyPageInfo(Long memberId, UpdateMyPageInfoRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return MemberInfoResponseDto.of(member.updateMemberInfo(requestDto));
    }
}
