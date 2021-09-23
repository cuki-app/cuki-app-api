package com.cuki.domain.comment.dto;

import com.cuki.domain.comment.entity.Comment;
import com.cuki.domain.member.entity.Member;
import com.cuki.domain.schedule.domain.Schedule;
import lombok.Getter;

@Getter
public class RegisterCommentRequestDto {

    private String content;
    private Long scheduleId;

    public Comment of(Member member, Schedule schedule) {
        return Comment.builder()
                .content(content.trim())
                .member(member)
                .schedule(schedule)
                .build();
    }
}
