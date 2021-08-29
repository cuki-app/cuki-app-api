package com.cuki.service;

import com.cuki.controller.dto.MemberInfoForAdminResponseDto;
import com.cuki.controller.dto.MemberInfoResponseDto;
import com.cuki.controller.dto.SignUpRequestDto;
import com.cuki.entity.Member;
import com.cuki.repository.MemberRepository;
import com.cuki.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberInfoResponseDto signup(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = signUpRequestDto.toMember(passwordEncoder);
        return MemberInfoResponseDto.of(memberRepository.save(member));
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
