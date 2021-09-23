package com.cuki.domain.comment.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessfullyRegisteredCommentResponseDto {
    private Long commentId;

}
