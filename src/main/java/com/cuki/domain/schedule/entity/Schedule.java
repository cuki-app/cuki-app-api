package com.cuki.domain.schedule.entity;

import com.cuki.domain.member.entity.Member;
import com.cuki.domain.model.BaseTimeEntity;
import com.cuki.domain.participation.entity.PermissionResult;
import lombok.*;
import javax.persistence.*;


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


    @OneToOne(cascade = CascadeType.ALL)
    private SchedulePeriod dateTime;

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

//    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)  //
//    private Set<Participation> participation = new HashSet<>();


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


    public void updateCurrentNumberOfPeople() {
        this.currentNumberOfPeople++;
        if (currentNumberOfPeople == fixedNumberOfPeople) {
            updateStatusToDone();
        }
    }

    public void updateNumberOfPeopleWaiting(PermissionResult result) {
        if (result.equals(PermissionResult.NONE)) {
            this.numberOfPeopleWaiting++;
        } else {
            this.numberOfPeopleWaiting--;
        }
    }

    //
    public boolean isNotOverFixedNumber() {
        return currentNumberOfPeople < fixedNumberOfPeople;
    }

    public void updateStatusToDone() {
        if (status.equals(ScheduleStatus.IN_PROGRESS)) {
            this.status = ScheduleStatus.DONE;
        }
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
        if (fixedNumberOfPeople < 2) {
            throw new IllegalArgumentException("모집인원은 2명 이상이어야 합니다.");
        }
    }

}
