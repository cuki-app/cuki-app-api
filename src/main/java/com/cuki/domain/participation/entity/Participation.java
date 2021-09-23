package com.cuki.domain.participation.entity;

<<<<<<< HEAD:src/main/java/com/cuki/domain/participation/domain/Participation.java
import com.cuki.domain.member.entity.Member;
import com.cuki.domain.schedule.domain.Schedule;
=======
import com.cuki.domain.member.domain.Member;
import com.cuki.domain.schedule.entity.Schedule;
>>>>>>> 6f02bde31d87f0f449b8ed90262a27719394f234:src/main/java/com/cuki/domain/participation/entity/Participation.java
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
