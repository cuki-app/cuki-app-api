package io.cuki.domain.schedule.dto;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class AllScheduleResponseDto {

    private final Long scheduleId;

    private String title;

    private String nickname;

    private String place;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int fixedNumberOfPeople;

    private int currentNumberOfPeople;

    private ScheduleStatus status;

    public static AllScheduleResponseDto of(Schedule schedule) {
        log.debug("AllScheduleResponseDto - of, 멤버 id = {}", schedule.getMember().getId());
        log.debug("AllScheduleResponseDto - of, 멤버 nickName = {}", schedule.getMember().getNickname());
        log.debug("AllScheduleResponseDto - of, 멤버 email = {}", schedule.getMember().getEmail());

        return AllScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .nickname(schedule.getMember().getNickname())
                .place(schedule.getLocation().getPlace())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                .status(schedule.getStatus())
                .build();
    }

}
