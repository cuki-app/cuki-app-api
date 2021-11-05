package io.cuki.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessfullyDeletedMemberResponseDto {
    private Long id;
}
