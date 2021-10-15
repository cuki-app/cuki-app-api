package io.cuki.domain.member.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Authority;
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
