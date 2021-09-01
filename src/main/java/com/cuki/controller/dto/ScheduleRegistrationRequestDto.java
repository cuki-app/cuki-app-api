package com.cuki.controller.dto;

import com.cuki.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class ScheduleRegistrationRequestDto {

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    private int participants;   // numberOfParticipants 로 변수명 바꿀 것

    private String place;

    private String description;


    // 쓸데 없이 객체 생성해서 사용하지는 않고 validation 으로만 씀
    public Schedule validation() {
        return new Schedule(title, new DateTime(startDateTime, endDateTime), participants, new Location(place), description);
    }


    // entity 생성할 때 유효성 검사 로직 타게 만들기 -> 안됨
    public Schedule of(Member member, DateTime dateTime, Participation participation, Location location) {
        return Schedule.builder()       // new ScheduleBuilder();
                .title(title)
                .member(member)
                .dateTime(dateTime)
                .participants(participants)
                .participation(participation)
                .location(location)
                .description(description)
                .build();   // new Schedule(title, user, dateTime.....description)
    }

}
