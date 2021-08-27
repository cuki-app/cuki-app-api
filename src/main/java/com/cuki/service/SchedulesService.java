package com.cuki.service;

import com.cuki.dto.*;
import com.cuki.entity.*;
import com.cuki.repository.SchedulesRepository;
import com.cuki.repository.UserRepository;
import com.cuki.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final UserRepository userRepository;
    private final SchedulesRepository schedulesRepository;


    // 로그인 완성되면 삭제
    private Long getCurrentUserId() {
        final String currentUsername = SecurityUtil.getCurrentUsername().orElseThrow(
                () -> new IllegalArgumentException("현재 접속한 유저가 아닙니다.")
        );
        final User currentUser = userRepository.findOneWithAuthoritiesByUsername(currentUsername).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );
        return currentUser.getId();
    }


    public MainScheduleResponseDto getMainSchedule() {
        List<AllScheduleResponseDto> allScheduleList = getAllSchedule();
        List<MyScheduleResponseDto> myScheduleList = getMySchedule();

        return MainScheduleResponseDto.builder()
                    .mySchedule(myScheduleList)
                    .allSchedule(allScheduleList)
                    .build();
    }



    public List<AllScheduleResponseDto> getAllSchedule() {
        final List<Schedule> repositoryAll = schedulesRepository.findAll();

        // 최신 순으로 정렬하기
        repositoryAll.sort(
                (a, b) -> b.getCreatedDate().compareTo(a.getCreatedDate())
        );

        List<AllScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : repositoryAll) {
            responseDtoList.add(
                    AllScheduleResponseDto.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .startDateTime(schedule.getDateTime().getStartDateTime())
                        .endDateTime(schedule.getDateTime().getEndDateTime())
                        .participants(schedule.getParticipants())
                        .place(schedule.getLocation().getPlace())
                        .description(schedule.getDescription())
                        .build());
        }
        return responseDtoList;
    }


    public List<MyScheduleResponseDto> getMySchedule() {
        final List<Schedule> allByUserId = schedulesRepository.findAllByUserId(getCurrentUserId());
        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : allByUserId) {
            responseDtoList.add(
                    MyScheduleResponseDto.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .startDateTime(schedule.getDateTime().getStartDateTime())
                        .endDateTime(schedule.getDateTime().getEndDateTime())
                        .participants(schedule.getParticipants())
                        .place(schedule.getLocation().getPlace())
                        .build()
            );
        }

        // D-day 임박순으로 정렬하기
        Collections.sort(responseDtoList);
        return responseDtoList;
    }


    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정 게시글 입니다.")
        );

        return OneScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .participants(schedule.getParticipants())
                .place(schedule.getLocation().getPlace())
                .description(schedule.getDescription())
                .build();
    }

    @Transactional
    public SimpleScheduleResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        final String currentUsername = SecurityUtil.getCurrentUsername().orElseThrow(
                () -> new IllegalArgumentException("현재 접속한 유저가 없습니다.")
        );
        log.info("current username = {}" ,currentUsername);
        final User currentUser = userRepository.findOneWithAuthoritiesByUsername(currentUsername).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final Schedule newSchedule = registrationRequestDto.of(currentUser, new DateTime(registrationRequestDto.getStartDateTime(), registrationRequestDto.getEndDateTime()), new Location(registrationRequestDto.getPlace()));

        final Long id = schedulesRepository.save(newSchedule).getId();

        return new SimpleScheduleResponseDto(id);
    }

    @Transactional
    public SimpleScheduleResponseDto deleteSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정 게시글 입니다.")
        );

        final Long writerId = schedule.getUser().getId();
        final Long scheduleIdFromRepository = schedule.getId();

        if (writerId.equals(getCurrentUserId())) {
            schedulesRepository.deleteById(scheduleId);
            // 삭제 후에 스케쥴 아이디 확인하면 나올까?
            System.out.println("해당 스케쥴 삭제 후 '객체' 확인 = " + schedule.toString());
        } else {
            throw new IllegalArgumentException("게시글 작성자만 삭제 할 수 있습니다.");
        }

        return new SimpleScheduleResponseDto(scheduleIdFromRepository);
    }
}
