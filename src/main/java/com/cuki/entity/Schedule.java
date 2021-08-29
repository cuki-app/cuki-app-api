package com.cuki.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Column(name= "title", length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private DateTime dateTime;

    @NotNull
    @Column(name = "participants")
    private int participants;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @NotNull
    @Column(name = "description", length = 300)
    private String description;


    public Schedule(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, int participants, Location location, String description) {
        checkTitleValidation(title);
        checkTimeValidation(startDateTime, endDateTime);
        checkParticipantsValidation(participants);
        checkLocationValidation(location);
        checkDetailValidation(description);
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

    private void checkParticipantsValidation(int participants) {
        if (participants < 2) {
            throw new IllegalArgumentException("참가인원은 2명 이상이어야 합니다.");
        }
    }

    private void checkTimeValidation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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
            throw new IllegalArgumentException("제목은 필수 값입니다. null이 되면 안됩니다.");
        }
        if (title.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("제목은 공백이 될 수 없습니다.");
        }
    }

}
