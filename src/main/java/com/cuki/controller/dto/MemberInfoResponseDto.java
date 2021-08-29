package com.cuki.controller.dto;

import com.cuki.entity.Authority;
import com.cuki.entity.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MemberInfoResponseDto {

    private String email;
    private String nickname;
    private Authority authority;

    public static MemberInfoResponseDto of(Member member) {
        return new MemberInfoResponseDto(member.getEmail(), member.getNickname(), member.getAuthority());
    }
}
