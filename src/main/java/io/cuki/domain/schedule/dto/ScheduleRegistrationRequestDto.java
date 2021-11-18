package io.cuki.domain.schedule.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.schedule.entity.Location;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.SchedulePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class ScheduleRegistrationRequestDto {

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private String place;

    private String details;


    public Schedule toEntity(Member member) {
        log.debug("ScheduleRegistrationRequestDto.toEntity - 호출");
        return Schedule.builder()
                .title(title)
                .member(member)
                .dateTime(new SchedulePeriod(startDateTime, endDateTime))
                .fixedNumberOfPeople(fixedNumberOfPeople)
                .location(new Location(place))
                .details(details)
                .build();
    }

    @Override
    public String toString() {
        return "ScheduleRegistrationRequestDto{" +
                "title='" + title + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", fixedNumberOfPeople=" + fixedNumberOfPeople +
                ", place='" + place + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
