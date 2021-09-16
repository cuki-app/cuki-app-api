package com.cuki.schedule.dto;

import com.cuki.entity.*;
import com.cuki.schedule.domain.DateTime;
import com.cuki.schedule.domain.Location;
import com.cuki.schedule.domain.Schedule;
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

    private int fixedNumberOfPeople;

    private String place;

    private String details;


    public Schedule of(Member member, DateTime dateTime, Location location) {
        return Schedule.builder()
                .title(title)
                .member(member)
                .dateTime(dateTime)
                .fixedNumberOfPeople(fixedNumberOfPeople)
                .currentNumberOfPeople(1)
                .location(location)
                .details(details)
                .build();
    }

}