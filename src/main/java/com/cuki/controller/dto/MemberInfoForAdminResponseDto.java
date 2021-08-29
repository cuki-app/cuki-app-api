package com.cuki.controller.dto;

import com.cuki.entity.Authority;
import com.cuki.entity.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MemberInfoForAdminResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private Authority authority;

    public static MemberInfoForAdminResponseDto of(Member member) {
        return new MemberInfoForAdminResponseDto(member.getId(), member.getEmail(), member.getNickname(), member.getAuthority());
    }
}
