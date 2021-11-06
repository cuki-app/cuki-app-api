package io.cuki.domain.comment.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class RegisterCommentRequestDto {

    private String content;

    public Comment of(Member member, Schedule schedule) {
        return Comment.builder()
                .content(content.trim())
                .member(member)
                .schedule(schedule)
                .build();
    }
}
