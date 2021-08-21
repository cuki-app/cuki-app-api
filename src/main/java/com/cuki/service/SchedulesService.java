package com.cuki.service;

import com.cuki.controller.common.ApiResponse;
import com.cuki.dto.ScheduleRegistrationRequestDto;
import com.cuki.entity.DateTime;
import com.cuki.entity.Location;
import com.cuki.entity.Schedule;
import com.cuki.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final SchedulesRepository schedulesRepository;


    @Transactional
    public ApiResponse<Long> save(ScheduleRegistrationRequestDto requestDto) {
        // user 는 추후에 추가

        /**
         * 1. controller -> service
         * 2. allDay -> t/f 검사 -> true 로직, false 로직
         * 3. 각 로직에 따른 DateTime 객체 빌드하기
         */
        final DateTime dateTime = getDateTime(requestDto);
//        final Location locations = getLocations(requestDto);

//        final Long id = schedulesRepository.save(requestDto.toEntity(dateTime)).getId();
//        return ApiResponse.ok(id);
        return ApiResponse.ok(null);//
    }

    private Location getLocations(ScheduleRegistrationRequestDto requestDto) {
        // List 말고 하나만 받는 걸로 짜기
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
                    0, 0,0);

            final LocalDateTime endDateTime = LocalDateTime.of(requestDto.getStartDateTime().getYear(),
                    requestDto.getStartDateTime().getMonthValue(),
                    requestDto.getStartDateTime().getDayOfMonth(),
                    23, 59,59);

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
    public ApiResponse<List<Schedule>> findAll() {
        final List<Schedule> all = schedulesRepository.findAll();
        return ApiResponse.ok(all);
    }
}
