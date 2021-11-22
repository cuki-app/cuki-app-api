package io.cuki.domain.schedule.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.model.BaseTimeEntity;
import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.participation.entity.PermissionResult;
import io.cuki.domain.participation.exception.FixedNumberOutOfBoundsException;
import io.cuki.domain.schedule.exception.InvaldDetailsException;
import io.cuki.domain.schedule.exception.InvalidFixedNumberException;
import io.cuki.domain.schedule.exception.ScheduleStatusIsAlreadyChangedException;
import io.cuki.domain.schedule.exception.InvalidTitleException;
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

    @Embedded
    private Location location;

    @Embedded
    private SchedulePeriod dateTime;    // period

    @Column(nullable = false)
    private Integer fixedNumberOfPeople;

    @Column(nullable = false)
    private Integer currentNumberOfPeople;

    @Column(length = 300, nullable = false)
    private String details;

    private Integer numberOfPeopleWaiting;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private Set<Participation> participants;



    @Builder
    public Schedule(String title, Member member, SchedulePeriod dateTime, Integer fixedNumberOfPeople, Location location, String details) {
        log.debug("Schedule.Schedule() - 호출");
        log.debug("생성자 - fixedNumberOfPeople = {}", fixedNumberOfPeople);
        checkTitleValidation(title);
        checkFixedNumberOfPeople(fixedNumberOfPeople);
        checkDetailsValidation(details);
        int WRITER_ONESELF = 1;
        int NONE = 0;

        this.title = title;
        this.member = member;
        this.dateTime = dateTime;
        this.fixedNumberOfPeople = (fixedNumberOfPeople + WRITER_ONESELF);
        this.currentNumberOfPeople = WRITER_ONESELF;
        this.location = location;
        this.details = details;
        this.numberOfPeopleWaiting = NONE;
        this.status = ScheduleStatus.IN_PROGRESS;
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

    private void updateCurrentNumberOfPeople() {
        this.currentNumberOfPeople++;
        if (currentNumberOfPeople.equals(fixedNumberOfPeople)) {
            updateStatusToDone();
        }
    }


    public void updateStatusToDone() {
        if (status == ScheduleStatus.IN_PROGRESS) {
            this.status = ScheduleStatus.DONE;
        }
    }

    public void isNotOverFixedNumber() {
        if (currentNumberOfPeople >= fixedNumberOfPeople) {
            log.error("확정자 = {}, 정원 = {}", currentNumberOfPeople, fixedNumberOfPeople);
            throw new FixedNumberOutOfBoundsException("정원이 초과되었습니다.");
        }
    }

    public void statusIsNotDone() {
        if (getStatus() == ScheduleStatus.DONE) {
            log.error("{}번 게시글은 이미 마감 처리되었습니다.", getId());
            throw new ScheduleStatusIsAlreadyChangedException("이미 마감 처리된 게시글 입니다.");
        }
    }


    private void checkDetailsValidation(String details) {
        if (details == null) {
            log.error("세부 설명 = {}", details);
            throw new InvaldDetailsException("세부 설명은 필수 입력 사항입니다.");
        }
        if (details.replace(" ", "").isEmpty()) {
            log.error("세부 설명은 공백 이거나 빈문자열이어서는 안됩니다. = {}", details);
            throw new InvaldDetailsException("세부 설명은 공백으로 두실 수 없습니다.");
        }
        if (details.length() > 300) {
            log.error("세부 설명이 300자를 초과했습니다. = {}/{}", details.length(), 300);
            throw new InvaldDetailsException("세부 설명은 300자를 초과하면 안됩니다.");
        }
    }


    private void checkTitleValidation(String title) {
        if (title == null) {
            log.error("제목 = {}", title);
            throw new InvalidTitleException("제목은 필수 입력 사항입니다.");
        }
        if (title.replace(" ", "").isEmpty()) {
            log.error("제목은 공백 이거나 빈문자열이어서는 안됩니다. = {}", title);
            throw new InvalidTitleException("제목은 공백으로 두실 수 없습니다.");
        }
    }


    private void checkFixedNumberOfPeople(Integer fixedNumberOfPeople) {
        log.debug("유효성 - fixedNumberOfPeople = {}", fixedNumberOfPeople);
        if (fixedNumberOfPeople < 1) {
            log.error("모집 인원은 1명 이상이어야 합니다. = {}명", fixedNumberOfPeople);
            throw new InvalidFixedNumberException("모집 인원을 1명 이상 입력해주세요.");
        }
    }

}
