package com.cuki.service;

import com.cuki.controller.dto.*;
import com.cuki.entity.*;
import com.cuki.repository.SchedulesRepository;
import com.cuki.repository.MemberRepository;
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

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;


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
                            .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                            .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                            .place(schedule.getLocation().getPlace())
                            .description(schedule.getDescription())
                            .build());
        }
        return responseDtoList;
    }


    public List<MyScheduleResponseDto> getMySchedule() {
        final List<Schedule> allByUserId = schedulesRepository.findAllByMemberId(SecurityUtil.getCurrentMemberId());
        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : allByUserId) {
            responseDtoList.add(
                    MyScheduleResponseDto.builder()
                            .id(schedule.getId())
                            .title(schedule.getTitle())
                            .startDateTime(schedule.getDateTime().getStartDateTime())
                            .endDateTime(schedule.getDateTime().getEndDateTime())
                            .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                            .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
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
                .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                .place(schedule.getLocation().getPlace())
                .description(schedule.getDescription())
                .build();
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

    /**
     * 모집 글을 게시한 본인이 참여하는 경우는 제외시킬 것 (o)
     * 중복 참여의 경우 제외시킬 것
     * 참여 하기를 두 번 요청한 경우 = 취소 or api 개별로
     */
    @Transactional
    public SimpleScheduleResponseDto joinSchedule(Long scheduleId) throws IllegalAccessException {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );

        final Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );  // 로그인 유저 == 참여하기 요청한 유저

        final int fixedNumberOfPeople = schedule.getFixedNumberOfPeople();
        int currentNumberOfPeople = schedule.getCurrentNumberOfPeople();

        if (!schedule.getMember().getId().equals(member.getId())) {     // 게시글 작성자 != 참여 원하는 자
            if (currentNumberOfPeople < fixedNumberOfPeople) {  // 1 < 2
                currentNumberOfPeople++;
                new Participation(member, schedule);    // 참여명단에 등록
                schedule.update(currentNumberOfPeople);
                return new SimpleScheduleResponseDto(schedule.getId());
            } else {
                throw new IllegalAccessException("모집이 마감되었습니다.");
            }
        } else {
            throw new IllegalAccessException("게시글 작성자는 본인의 모집 일정에 참여하기 기능을 사용할 수 없습니다.");
        }

    }
}
