package com.cuki.domain.member.service;

import com.cuki.domain.member.dto.MemberInfoForAdminResponseDto;
import com.cuki.domain.member.dto.MemberInfoResponseDto;
import com.cuki.domain.member.repository.MemberRepository;
import com.cuki.global.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberInfoResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

    }

    @Transactional(readOnly = true)
    public MemberInfoForAdminResponseDto getMemberInfo(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberInfoForAdminResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }
}
