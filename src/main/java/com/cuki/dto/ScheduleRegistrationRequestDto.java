package com.cuki.dto;

import com.cuki.entity.DateTime;
import com.cuki.entity.Location;
import com.cuki.entity.Schedule;
import com.cuki.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class ScheduleRegistrationRequestDto {

    @NotNull
    private String title;

    private boolean allDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @NotNull
    private int participants;

    @NotNull
    private String place;

    private String roadNameAddress;
    private String numberAddress;

    @NotNull
    private int latitudeX;

    @NotNull
    private int longitudeY;

    @NotNull
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
