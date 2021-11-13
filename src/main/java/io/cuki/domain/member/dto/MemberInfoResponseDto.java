package io.cuki.domain.member.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Authority;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoResponseDto {

    private Long memberId;
    private String email;
    private String nickname;

    public static MemberInfoResponseDto of(Member member) {
        return new MemberInfoResponseDto(member.getId(), member.getEmail(), member.getNickname());
    }
}
