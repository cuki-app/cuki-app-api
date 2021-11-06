package io.cuki.domain.schedule.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.schedule.entity.Location;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.SchedulePeriod;
import io.cuki.domain.schedule.entity.ScheduleStatus;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private String place;

    private String details;


    public Schedule toEntity(Member member) {
        final int ONESELF = 1;
        return Schedule.builder()
                .title(title)
                .member(member)
                .dateTime(new SchedulePeriod(startDateTime, endDateTime))
                .fixedNumberOfPeople(fixedNumberOfPeople)
                .currentNumberOfPeople(ONESELF)   //
                .location(new Location(place))
                .details(details)
                .status(ScheduleStatus.IN_PROGRESS) //
                .build();
    }

}
