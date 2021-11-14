package io.cuki.domain.comment.dto;

import io.cuki.domain.comment.entity.CommentAuthority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private final Long commentId;
    private final Long writerId;
    private final String nickname;
    private final String content;
    private final String textDate;
    private final CommentAuthority commentAuthority;
    private final LocalDateTime createdDate;

}
