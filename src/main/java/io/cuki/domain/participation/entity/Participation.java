package io.cuki.domain.participation.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.participation.exception.InvalidReasonForParticipationException;
import io.cuki.domain.participation.exception.PermissionIsAlreadyDecidedException;
import io.cuki.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;

@Slf4j
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
        checkReasonForParticipation(reasonForParticipation);
        this.member = member;
        this.schedule = schedule;
        this.reasonForParticipation = reasonForParticipation;
        this.result = PermissionResult.NONE;
    }

    public static Participation create(Member member, Schedule schedule, String reasonForParticipation) {
        return new Participation(member, schedule, reasonForParticipation);
    }

    public void updateResult(PermissionResult result) {
        if (isNotYetDecidedOnPermission()) { // accept or reject
            this.result = result;
        }
    }

    private boolean isNotYetDecidedOnPermission() {
        if (getResult() != PermissionResult.NONE) {
            throw new PermissionIsAlreadyDecidedException("참여 여부가 이미 결정이 되었습니다.");
        }
        return true;
    }

    private void checkReasonForParticipation(String reasonForParticipation) {
        if (reasonForParticipation == null) {
            log.error("참여 이유 = {}", reasonForParticipation);
            throw new InvalidReasonForParticipationException("참여 이유를 입력해주세요.");
        }

        if (reasonForParticipation.trim().isEmpty()) {
            log.error("참여 이유는 공백 이거나 빈문자열이어서는 안됩니다. = {}", reasonForParticipation);
            throw new InvalidReasonForParticipationException("참여 이유는 공백으로 두실 수 없습니다.");
        }
    }
}
