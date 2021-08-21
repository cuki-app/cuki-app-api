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
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class ScheduleRegistrationRequestDto {

    private String title;

//    private DateTime dateTime;
    private boolean allDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;    // 2021.08.20.12:34:30

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    private int participants;

//    private List<Location> locations;
    private String place;
    private String roadNameAddress;
    private String numberAddress;
    private int latitudeX;
    private int longitudeY;

    private String description;


    public Schedule toEntity(User user, DateTime dateTime, Location location) {
        return Schedule.builder()
                .title(title)
                .user(user)
                .dateTime(dateTime)
                .participants(participants)
                .location(location)
                .description(description)
                .build();
    }

}
