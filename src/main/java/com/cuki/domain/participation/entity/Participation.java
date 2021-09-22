package com.cuki.domain.participation.entity;

import com.cuki.domain.member.domain.Member;
import com.cuki.domain.schedule.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;      // kelly = 13 | kelly = 13

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;  // schedule id = 2 | schedule id = 6

    private String reasonForParticipation;


    private PermissionResult result;


    public Participation(Member member, Schedule schedule, String reasonForParticipation) {
        this.member = member;
        this.schedule = schedule;
        this.reasonForParticipation = reasonForParticipation;
        this.result = PermissionResult.NONE;
    }

    public void updateResult(PermissionResult result) {
        this.result = result;
    }
}
