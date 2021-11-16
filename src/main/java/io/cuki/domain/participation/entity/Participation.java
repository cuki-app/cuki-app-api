package io.cuki.domain.participation.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.participation.exception.PermissionIsAlreadyDecidedException;
import io.cuki.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Builder
@AllArgsConstructor
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
    private Member member;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String reasonForParticipation;

    private PermissionResult result;


    public Participation(Member member, Schedule schedule, String reasonForParticipation) {
        this.member = member;
        this.schedule = schedule;
        this.reasonForParticipation = reasonForParticipation;
        this.result = PermissionResult.NONE;
    }

    public void updateResult(PermissionResult result) {
        if (isNotYetDecidedOnPermission()) {
            this.result = result;
        }
    }

    private boolean isNotYetDecidedOnPermission() {
        if (getResult() != PermissionResult.NONE) {
            throw new PermissionIsAlreadyDecidedException("참여 여부가 이미 결정이 되었습니다.");
        }
        return true;
    }
}
