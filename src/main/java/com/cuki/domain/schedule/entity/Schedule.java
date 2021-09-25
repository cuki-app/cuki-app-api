package com.cuki.domain.schedule.entity;

import com.cuki.domain.member.entity.Member;
import com.cuki.domain.model.BaseTimeEntity;
import com.cuki.domain.participation.entity.Participation;
import com.cuki.domain.participation.entity.PermissionResult;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @Column(length = 300, nullable = false)
    private String details;

    private int numberOfPeopleWaiting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)  //
    private Set<Participation> participation = new HashSet<>();


    @Builder
    public Schedule(String title, Member member, SchedulePeriod dateTime, int fixedNumberOfPeople, int currentNumberOfPeople, Location location, String details, ScheduleStatus status) {
        System.out.println("validation하는 Schedule 생성자 호출");
        checkTitleValidation(title);
//        checkDetailValidation(details);

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
            updateStatus();
        }
    }

    public void updateNumberOfPeopleWaiting(PermissionResult result) {
        if (result.equals(PermissionResult.NONE)) {
            this.numberOfPeopleWaiting++;
        } else {
            this.numberOfPeopleWaiting--;
        }
    }

    public boolean isNotOverFixedNumber() {
        return currentNumberOfPeople < fixedNumberOfPeople;
    }

    public void updateStatus() {
        this.status = ScheduleStatus.DONE;
    }

    private void checkDetailValidation(String description) {
        if (description == null) {
            throw new IllegalArgumentException("세부 설명이 null이면 안됩니다.");
        }
        if (description.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("세부 설명은 공백 이거나 빈문자열이어서는 안됩니다.");
        }
        if (description.length() > 300) {
            throw new IllegalArgumentException("세부 설명은 300자를 초과하면 안됩니다.");
        }
    }

    private void checkLocationValidation(Location location) {
        if (location.getPlace() == null) {
            throw new NullPointerException("위치가 null이면 안됩니다.");
        }
        if (location.getPlace().replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("위치가 공백 이거나 빈문자열이어서는 안됩니다.");
        }
    }

    private void checkParticipantsValidation(Participation participation) {
//        if (participation.getNumberOfParticipants() < 2) {
//            throw new IllegalArgumentException("참가인원은 2명 이상이어야 합니다.");
//        }
//        this.participation = participation;
    }


    private void checkTimeValidation(SchedulePeriod dateTime) {
        LocalDateTime startDateTime = dateTime.getStartDateTime();
        LocalDateTime endDateTime = dateTime.getEndDateTime();

        if (startDateTime == null || endDateTime == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            if (!(startDateTime.getDayOfYear() == LocalDateTime.now().getDayOfYear()
                    && startDateTime.getMonthValue() == LocalDateTime.now().getMonthValue()
                    && startDateTime.getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
            ) {
                throw new IllegalArgumentException("시작일은 현재보다 과거가 될 수 없습니다.");
            }
        }

        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("종료일은 시작일보다 과거가 될 수 없습니다.");
        }

    }



    private void checkTitleValidation(String title) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 Null 일 수 없습니다.");
        }
        if (title.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("제목은 공백이거나 빈 문자열일 수 없습니다.");
        }
    }

}
