package com.cuki.domain.schedule.dto;

import com.cuki.domain.member.entity.Member;
import com.cuki.domain.schedule.entity.SchedulePeriod;
import com.cuki.domain.schedule.entity.Location;
import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.entity.ScheduleStatus;
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


    public Schedule toEntity(Member member) {
        return Schedule.builder()
                .title(title)
                .member(member)
                .dateTime(new SchedulePeriod(startDateTime, endDateTime))
                .fixedNumberOfPeople(fixedNumberOfPeople)
                .currentNumberOfPeople(1)   //
                .location(new Location(place))
                .details(details)
                .status(ScheduleStatus.IN_PROGRESS) //
                .build();
    }

}
