package com.cuki.schedule.service;

import com.cuki.entity.*;
import com.cuki.schedule.repository.SchedulesRepository;
import com.cuki.repository.MemberRepository;
import com.cuki.schedule.domain.DateTime;
import com.cuki.schedule.domain.Location;
import com.cuki.schedule.domain.Schedule;
import com.cuki.schedule.dto.*;
import com.cuki.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;


    public Page<Schedule> getAllSchedulesUsingPaging(Pageable pageable) {
        return schedulesRepository.findAll(pageable);
    }


    public Page<Schedule> getScheduleByQueryMethod(Pageable pageable) {

        final Page<Schedule> allByMemberNickname = schedulesRepository.findAllByMemberNickname("무적토마토", pageable);

        for (Schedule schedule : allByMemberNickname) {
            log.info("닉네임으로 찾은 스케쥴 - 닉네임= {}", schedule.getMember().getNickname());
            log.info("닉네임으로 찾은 스케쥴 - 스케쥴 아이디 = {}", schedule.getId());
        }

        return schedulesRepository.findAllByMemberNickname("무적토마토", pageable);
    }

    // page test
    public void initializing() {
        // 왜 를 생각해볼 것
//        final Member member = Member.builder()
//                .nickname("테스트 사용자")
//                .build();

        for (int i = 0; i < 30; i++) {
            Schedule schedule = Schedule.builder()
                    .title("북카페 모임 " + (i+1))
                    .dateTime(new DateTime(LocalDateTime.of(2021, 9, 21, 21, 32), LocalDateTime.of(2021, 10, 13, 21, 30)))
                    .fixedNumberOfPeople(3)
                    .currentNumberOfPeople(1)
                    .location(new Location("합정 교보문고"))
                    .details("더미 데이터 " + (i+1))
                    .build();
            schedulesRepository.save(schedule);
        }
    }

    @Transactional
    public SimpleScheduleResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        final Member currentMember = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final Schedule newSchedule =
                registrationRequestDto.of(currentMember, new DateTime(registrationRequestDto.getStartDateTime(), registrationRequestDto.getEndDateTime()), new Location(registrationRequestDto.getPlace())
                );

        final Long id = schedulesRepository.save(newSchedule).getId();

        return new SimpleScheduleResponseDto(id);
    }

    // 메인   // getter test
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
                            .scheduleId(schedule.getId())
                            .title(schedule.getTitle())
                            .place(schedule.getLocation().getPlace())
                            .startDateTime(schedule.getDateTime().getStartDateTime())
                            .endDateTime(schedule.getDateTime().getEndDateTime())
                            .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                            .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                            .build());
        }
        return responseDtoList;
    }

    // 일정 상세 조회
    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정 게시글 입니다.")
        );

        return OneScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                .place(schedule.getLocation().getPlace())
                .details(schedule.getDetails())
                .numberOfPeopleWaiting(schedule.getNumberOfPeopleWaiting())
                .build();
    }


    // 내가 등록한 모집 일정 전체 보여주기
    public List<MyScheduleResponseDto> getMySchedule() {
        final List<Schedule> allByUserId = schedulesRepository.findAllByMemberId(SecurityUtil.getCurrentMemberId());
        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : allByUserId) {
            responseDtoList.add(
                    MyScheduleResponseDto.builder()
                            .scheduleId(schedule.getId())
                            .title(schedule.getTitle())
                            .startDateTime(schedule.getDateTime().getStartDateTime())
                            .endDateTime(schedule.getDateTime().getEndDateTime())
                            .build()
            );
        }

        // D-day 임박순으로 정렬하기
        Collections.sort(responseDtoList);
        return responseDtoList;
    }


    @Transactional
    public SimpleScheduleResponseDto deleteSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정 게시글 입니다.")
        );

        final Long writerId = schedule.getMember().getId();
        final Long scheduleIdFromRepository = schedule.getId();

        if (writerId.equals(SecurityUtil.getCurrentMemberId())) {
            schedulesRepository.delete(schedule);
            System.out.println("해당 스케쥴 삭제 후 '객체' 확인 = " + schedule.toString());
        } else {
            throw new IllegalArgumentException("게시글 작성자만 삭제 할 수 있습니다.");
        }

        return new SimpleScheduleResponseDto(scheduleIdFromRepository);
    }
}
