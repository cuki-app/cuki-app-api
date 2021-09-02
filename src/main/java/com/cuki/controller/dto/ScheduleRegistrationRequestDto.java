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


    public Schedule of(Member member, DateTime dateTime, Participation participation, Location location) {
        return Schedule.builder()
                .title(title)
                .member(member)
                .dateTime(dateTime)
                .participation(participation)
                .location(location)
                .description(description)
                .build();
    }

}
