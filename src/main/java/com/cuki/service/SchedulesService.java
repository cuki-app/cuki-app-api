package com.cuki.service;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.DetailedScheduleResponseDto;
import com.cuki.dto.ScheduleResponseDto;
import com.cuki.dto.MyScheduleResponseDto;
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
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final UserRepository userRepository;
    private final SchedulesRepository schedulesRepository;


    private Long getCurrentUserId() {
        final String currentUsername = SecurityUtil.getCurrentUsername().orElseThrow(
                () -> new IllegalArgumentException("현재 접속한 유저가 아닙니다.")
        );
        final User currentUser = userRepository.findOneWithAuthoritiesByUsername(currentUsername).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );
        return currentUser.getId();
    }

    @Transactional
    public ApiResponse<Long> createSchedule(ScheduleRegistrationRequestDto requestDto) {
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



    public ApiResponse<Map<String, List<?>>> readAllSchedule() {
        Map<String, List<?>> mySkedAndAllSked = new HashMap<>();

        final List<MyScheduleResponseDto> mySchedule = getMySchedule();
        List<ScheduleResponseDto> allSchedule = getAllSchedule();

        Collections.sort(mySchedule);
        mySkedAndAllSked.put("mySchedule", mySchedule);
        mySkedAndAllSked.put("allSchedule",allSchedule);

        return ApiResponse.ok(mySkedAndAllSked);
    }

    private List<ScheduleResponseDto> getAllSchedule() {
        final List<Schedule> allSchedules = schedulesRepository.findAll();
        List<ScheduleResponseDto> schduleRespDto = new ArrayList<>();

        for (Schedule schedule : allSchedules) {
            schduleRespDto.add(
                    ScheduleResponseDto.builder()
                            .id(schedule.getId())
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
        return schduleRespDto;
    }

    private List<MyScheduleResponseDto> getMySchedule() {
        final List<Schedule> scheduleListById = schedulesRepository.findAllByUserId(getCurrentUserId());
        List<MyScheduleResponseDto> scheduleRespDtoByUser = new ArrayList<>();

        for (Schedule schedule : scheduleListById) {
            scheduleRespDtoByUser.add(
                    MyScheduleResponseDto.builder()
                            .id(schedule.getId())
                            .title(schedule.getTitle())
                            .allDay(schedule.getDateTime().isAllDay())
                            .startDateTime(schedule.getDateTime().getStartDateTime())
                            .endDateTime(schedule.getDateTime().getEndDateTime())
                            .participants(schedule.getParticipants())
                            .place(schedule.getLocation().getPlace())
                            .build()
            );
        } // for

        return scheduleRespDtoByUser;
    }


    @Transactional
    public ApiResponse<Long> deleteSchedule(Long id) {
        final Schedule schedule = schedulesRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("현존하지 않는 일정 입니다.")
        );
        if (schedule.getUser().getId().equals(getCurrentUserId())) {
            schedulesRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("본인의 일정만 삭제 할 수 있습니다.");
        }

        return ApiResponse.ok(id);
    }

    public ApiResponse<DetailedScheduleResponseDto> readDetailedSchedule(Long id) {
        final Schedule schedule = schedulesRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("현존하지 않는 일정 입니다.")
        );
        final DetailedScheduleResponseDto detailedSkedDto =
                    DetailedScheduleResponseDto.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .allDay(schedule.getDateTime().isAllDay())
                        .startDateTime(schedule.getDateTime().getStartDateTime())
                        .endDateTime(schedule.getDateTime().getEndDateTime())
                        .participants(schedule.getParticipants())
                        .place(schedule.getLocation().getPlace())
                        .roadNameAddress(schedule.getLocation().getRoadNameAddress())
                        .latitudeX(schedule.getLocation().getLatitudeX())
                        .longitudeY(schedule.getLocation().getLongitudeY())
                        .description(schedule.getDescription())
                        .build();

        return ApiResponse.ok(detailedSkedDto);
    }
}
