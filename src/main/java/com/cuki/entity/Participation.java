package com.cuki.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;      // kelly = 13 | kelly = 13

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;  // schedule id = 2 | schedule id = 6

    private String reasonForParticipation;


    public Participation(Member member, Schedule schedule) {
        this.member = member;
        this.schedule = schedule;
    }

    public Participation(Member member, Schedule schedule, String reasonForParticipation) {
        this.member = member;
        this.schedule = schedule;
        this.reasonForParticipation = reasonForParticipation;
    }
}
