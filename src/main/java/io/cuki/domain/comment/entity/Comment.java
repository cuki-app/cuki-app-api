package io.cuki.domain.comment.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.model.BaseTimeEntity;
import io.cuki.domain.schedule.entity.Schedule;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
}
