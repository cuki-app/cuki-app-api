package com.cuki.service;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.AllScheduleResponseDto;
import com.cuki.dto.ScheduleRegistrationRequestDto;
import com.cuki.entity.DateTime;
import com.cuki.entity.Location;
import com.cuki.entity.Schedule;
import com.cuki.entity.User;
import com.cuki.repository.SchedulesRepository;
import com.cuki.repository.UserRepository;
import com.cuki.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final UserRepository userRepository;
    private final SchedulesRepository schedulesRepository;


    @Transactional
    public ApiResponse<Long> save(ScheduleRegistrationRequestDto requestDto) {
        final String currentUsername = SecurityUtil.getCurrentUsername().orElseThrow(
                () -> new IllegalArgumentException("현재 접속한 유저가 아닙니다.")
        );
        log.info("current username = {}" ,currentUsername);
        final User currentUser = userRepository.findOneWithAuthoritiesByUsername(currentUsername).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final DateTime dateTime = getDateTime(requestDto);
        final Location location = getLocation(requestDto);

        final Long id = schedulesRepository.save(requestDto.toEntity(currentUser, dateTime, location)).getId();
        return ApiResponse.ok(id);
    }

    private Location getLocation(ScheduleRegistrationRequestDto requestDto) {
        return Location.builder()
                .place(requestDto.getPlace())
                .roadNameAddress(requestDto.getRoadNameAddress())
                .numberAddress(requestDto.getNumberAddress())
                .latitudeX(requestDto.getLatitudeX())
                .longitudeY(requestDto.getLongitudeY())
                .build();
    }

    private DateTime getDateTime(ScheduleRegistrationRequestDto requestDto) {
        if (requestDto.isAllDay()) {    // allDay == true
            final LocalDateTime startDateTime = LocalDateTime.of(requestDto.getStartDateTime().getYear(),
                    requestDto.getStartDateTime().getMonthValue(),
                    requestDto.getStartDateTime().getDayOfMonth(),
                    0, 0);

            final LocalDateTime endDateTime = LocalDateTime.of(requestDto.getStartDateTime().getYear(),
                    requestDto.getStartDateTime().getMonthValue(),
                    requestDto.getStartDateTime().getDayOfMonth(),
                    23, 59);

            System.out.println("startDateTime = " + startDateTime);
            System.out.println("endDateTime = " + endDateTime);
            return DateTime.builder()
                    .allDay(true)
                    .startDateTime(startDateTime)
                    .endDateTime(endDateTime)
                    .build();
        } else {
            return DateTime.builder()
                    .allDay(requestDto.isAllDay())
                    .startDateTime(requestDto.getStartDateTime())
                    .endDateTime(requestDto.getEndDateTime())
                    .build();
        }
    }


    // responseDto 로 내려주기
    public ApiResponse<List<AllScheduleResponseDto>> findAll() {
        final List<Schedule> allSchedules = schedulesRepository.findAll();
        List<AllScheduleResponseDto> allDtoList = new ArrayList<>();

        for (Schedule schedule : allSchedules) {
            if (schedule.getDateTime().isAllDay()) {    // allDay == true
                allDtoList.add(
                        AllScheduleResponseDto.builder()
                                .title(schedule.getTitle())
                                .allDay(schedule.getDateTime().isAllDay())
                                .startDateTime(schedule.getDateTime().getStartDateTime())
                                .participants(schedule.getParticipants())
                                .place(schedule.getLocation().getPlace())
                                .description(schedule.getDescription())
                                .build()
                );
            } else {
                allDtoList.add(
                    AllScheduleResponseDto.builder()
                                .title(schedule.getTitle())
                                .allDay(schedule.getDateTime().isAllDay())
                                .startDateTime(schedule.getDateTime().getStartDateTime())
                                .endDateTime(schedule.getDateTime().getEndDateTime())
                                .participants(schedule.getParticipants())
                                .place(schedule.getLocation().getPlace())
                                .description(schedule.getDescription())
                                .build()
                );
            }
        } // for

        return ApiResponse.ok(allDtoList);
    }
}
