package io.cuki.domain.member.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Authority;
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
