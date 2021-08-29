package com.cuki.controller.dto;

import com.cuki.entity.DateTime;
import com.cuki.entity.Location;
import com.cuki.entity.Schedule;
import com.cuki.entity.Member;
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

    private int participants;

    private String place;

    private String description;


    public Schedule of(Member member, DateTime dateTime, Location location) {
        return Schedule.builder()       // new ScheduleBuilder();
                .title(title)
                .member(member)
                .dateTime(dateTime)
                .participants(participants)
                .location(location)
                .description(description)
                .build();   // new Schedule(title, user, dateTime.....description)
    }

}
