package io.cuki.domain.schedule.dto;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class MyScheduleResponseDto implements Comparable<MyScheduleResponseDto> {

    private Long scheduleId;

    private String title;

    private String nickname;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private ScheduleStatus status;


    public static MyScheduleResponseDto of(Schedule schedule) {
        return MyScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .nickname(schedule.getMember().getNickname())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .status(schedule.getStatus())
                .build();
    }

    @Override
    public int compareTo(MyScheduleResponseDto o) {

        final LocalDateTime now = LocalDateTime.now();
        final long dDays = Duration.between(now, getStartDateTime()).toDays();
        final long targetDdays = Duration.between(now, o.getStartDateTime()).toDays();

        return Long.compare(dDays, targetDdays);
    }

}
