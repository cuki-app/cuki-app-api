package io.cuki.domain.schedule.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.model.BaseTimeEntity;
import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.participation.entity.PermissionResult;
import io.cuki.domain.participation.exception.FixedNumberOutOfBoundsException;
import io.cuki.domain.schedule.exception.ScheduleStatusIsAlreadyChangedException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import java.util.Set;

@Slf4j
@Getter
@NoArgsConstructor
@Entity
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;


    @OneToOne(cascade = CascadeType.ALL)
    private Location location;


    @Embedded
    private SchedulePeriod dateTime;    // period

    @Column(nullable = false)
    private int fixedNumberOfPeople;

    @Column(nullable = false)
    private int currentNumberOfPeople;


    @Column(length = 300, nullable = false)
    private String details;

    private int numberOfPeopleWaiting;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private Set<Participation> participants;



    @Builder
    public Schedule(String title, Member member, SchedulePeriod dateTime, int fixedNumberOfPeople, int currentNumberOfPeople, Location location, String details, ScheduleStatus status) {
        System.out.println("validation 하는 Schedule 생성자 호출");
        checkTitleValidation(title);
        checkFixedNumberOfPeople(fixedNumberOfPeople);
        checkDetailsValidation(details);

        this.title = title;
        this.member = member;
        this.dateTime = dateTime;
        this.fixedNumberOfPeople = fixedNumberOfPeople;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.location = location;
        this.details = details;
        this.status = status;
    }


    public void updateCurrentNumberOfPeople() { // 어디서 사용하는지 확인할 것
        this.currentNumberOfPeople++;
        if (currentNumberOfPeople == fixedNumberOfPeople) {
            updateStatusToDone();
        }
    }

    public void updateNumberOfPeopleWaiting(PermissionResult result) {
        if (result == PermissionResult.NONE) {
            this.numberOfPeopleWaiting++;
        } else {   // accept or reject
            this.numberOfPeopleWaiting--;
            if (result == PermissionResult.ACCEPT) {
                updateCurrentNumberOfPeople();
            }
        }
    }


    public boolean isNotOverFixedNumber() {
        if (currentNumberOfPeople >= fixedNumberOfPeople) {
            throw new FixedNumberOutOfBoundsException("정원이 초과되었습니다.");
        }
        return true;
    }

    public void updateStatusToDone() {
        if (status == ScheduleStatus.IN_PROGRESS) {
            this.status = ScheduleStatus.DONE;
        }
    }

    public boolean statusIsNotDone() throws ScheduleStatusIsAlreadyChangedException {
        if (getStatus() == ScheduleStatus.DONE) {
            log.debug("{}번 게시글은 이미 마감 처리되었습니다.", getId());
            throw new ScheduleStatusIsAlreadyChangedException("이미 마감 처리된 게시글 입니다.");
        }
        return true;
    }



    // 어노테이션으로 대체 가능
    private void checkDetailsValidation(String details) {
        if (details == null) {
            throw new IllegalArgumentException("세부 설명이 null 이면 안됩니다.");
        }
        if (details.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("세부 설명은 공백 이거나 빈문자열이어서는 안됩니다.");
        }
        if (details.length() > 300) {
            throw new IllegalArgumentException("세부 설명은 300자를 초과하면 안됩니다.");
        }
    }

    // 어노테이션으로 대체 가능
    private void checkTitleValidation(String title) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 Null 일 수 없습니다.");
        }
        if (title.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("제목은 공백이거나 빈 문자열일 수 없습니다.");
        }
    }

    // 어노테이션으로 대체 가능
    private void checkFixedNumberOfPeople(int fixedNumberOfPeople) {
        if (fixedNumberOfPeople < 1) {
            throw new IllegalArgumentException("모집인원은 1명 이상이어야 합니다.");
        }
    }

}
