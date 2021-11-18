package io.cuki.domain.member.service;

import io.cuki.domain.member.dto.MemberInfoResponseDto;
import io.cuki.domain.member.dto.UpdateMyPageInfoRequestDto;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Nickname;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.member.exception.NicknameAlreadyExistException;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.global.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 현재 접속중인 회원 정보 조회
    public MemberInfoResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberInfoResponseDto::of)
                .orElseThrow(MemberNotFoundException::new);

    }

    // 마이페이지 조회
    public MemberInfoResponseDto getMyPageInfo(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberInfoResponseDto::of)
                .orElseThrow(MemberNotFoundException::new);
    }

    // 마이페이지 수정
    @Transactional
    public MemberInfoResponseDto updateMyPageInfo(Long memberId, UpdateMyPageInfoRequestDto requestDto) {
        String nickname = requestDto.getNickname();

        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        // 2. 닉네임 중복 확인
        if (memberRepository.existsByNickname(new Nickname(nickname))) {
            log.error("닉네임 중복 확인 : 해당 닉네임은 이미 존재합니다. -> {}", nickname);
            throw new NicknameAlreadyExistException();
        }

        // 3. 닉네임 검증
        if (member.getNickname().isValidNickname(nickname)) {
            member.updateMemberInfo(requestDto);
        }

        return MemberInfoResponseDto.of(member);
    }
}
