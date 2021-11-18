package io.cuki.domain.comment.entity;

import io.cuki.domain.comment.exception.CommentNotValidException;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.model.BaseTimeEntity;
import io.cuki.domain.schedule.entity.Schedule;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 1000)
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "schedule_id")
    private Schedule schedule;

    @Builder
    private Comment(String content, Member member, Schedule schedule) {
        this.content = content;
        this.member = member;
        this.schedule = schedule;
    }

    public static boolean isValidContent(String content) {
        if (content == null) {
            log.error("댓글은 null 일 수 없습니다. -> {}", content);
            throw new CommentNotValidException("댓글을 입력해주세요.");
        } else if (content.trim().isEmpty()) {
            log.error("댓글은 공백 일 수 없습니다. -> {}", content);
            throw new CommentNotValidException("댓글을 입력해주세요.");
        } else if (content.length() > 1000) {
            log.error("댓글은 1000자 이하이어야 합니다. -> {}", content.length());
            throw new CommentNotValidException("1000자 이하로 입력해주세요.");
        }

        return true;
    }
}
