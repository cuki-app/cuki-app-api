package io.cuki.domain.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessfullyDeletedCommentResponseDto {
    private Long commentId;

}
