package com.cuki.dto;

import com.cuki.entity.DateTime;
import com.cuki.entity.Location;
import com.cuki.entity.Schedule;
import com.cuki.entity.User;
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


    public Schedule of(User user, DateTime dateTime, Location location) {
        return Schedule.builder()       // new ScheduleBuilder();
                .title(title)
                .user(user)
                .dateTime(dateTime)
                .participants(participants)
                .location(location)
                .description(description)
                .build();   // new Schedule(title, user, dateTime.....description)
    }

}
