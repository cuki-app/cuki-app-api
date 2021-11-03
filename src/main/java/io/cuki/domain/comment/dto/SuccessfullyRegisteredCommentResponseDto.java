package io.cuki.domain.comment.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessfullyRegisteredCommentResponseDto {
    private Long commentId;

}
