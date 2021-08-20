package com.cuki.dto;

import com.cuki.entity.DateTime;
import com.cuki.entity.Location;
import com.cuki.entity.Schedule;
import com.cuki.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class ScheduleRegistrationRequestDto {

    private String title;
//    private User user;

//    private DateTime dateTime;
    private boolean allDay;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;    // 2021.08.20.12:34:30

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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


//    public Schedule toEntity(DateTime dateTime, List<Location> locations) {
//        return Schedule.builder()
//                .title(title)
////                .user(user)
//                .dateTime(dateTime)
//                .participants(participants)
////                .locations(locations)
//                .description(description)
//                .build();
//    }

    public Schedule toEntity(DateTime dateTime) {
        return Schedule.builder()
                .title(title)
//                .user(user)
                .dateTime(dateTime)
                .participants(participants)
//                .locations(locations)
                .description(description)
                .build();
    }

}
