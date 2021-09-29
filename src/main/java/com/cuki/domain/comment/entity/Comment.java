package com.cuki.domain.comment.entity;

import com.cuki.domain.member.entity.Member;
import com.cuki.domain.model.BaseTimeEntity;
import com.cuki.domain.schedule.entity.Schedule;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 2000)
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, name = "schedule_id")
    private Schedule schedule;

}
