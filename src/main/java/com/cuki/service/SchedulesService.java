package com.cuki.service;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.AllScheduleResponseDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;

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
//                    .allDay(true)
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



    public ApiResponse<Map<String, List<?>>> findAll() {
        Map<String, List<?>> mySkedAndAllSked = new HashMap<>();
        // 내가 등록한 스케쥴 전부 보여주기
        final List<Schedule> myScheduleList = schedulesRepository.findAllByUserId(getCurrentUserId());
        List<MyScheduleResponseDto> myDtoList = new ArrayList<>();

        for (Schedule schedule : myScheduleList) {
            myDtoList.add(
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
        Collections.sort(myDtoList);


        final List<Schedule> allSchedules = schedulesRepository.findAll();
        List<AllScheduleResponseDto> allDtoList = new ArrayList<>();


        for (Schedule schedule : allSchedules) {
            if (schedule.getDateTime().isAllDay()) {    // allDay == true
                allDtoList.add(
                        AllScheduleResponseDto.builder()
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
            } else {
                allDtoList.add(
                    AllScheduleResponseDto.builder()
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
        } // for

        mySkedAndAllSked.put("mySchedule", myDtoList);
        mySkedAndAllSked.put("all",allDtoList);

        return ApiResponse.ok(mySkedAndAllSked);
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
}
