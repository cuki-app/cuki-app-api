package com.cuki.domain.schedule.service;

import com.cuki.domain.schedule.utils.WriterVerification;
import com.cuki.domain.member.entity.Member;
import com.cuki.domain.schedule.repository.SchedulesRepository;
import com.cuki.domain.member.repository.MemberRepository;
import com.cuki.domain.schedule.domain.DateTime;
import com.cuki.domain.schedule.domain.Location;
import com.cuki.domain.schedule.domain.Schedule;
import com.cuki.domain.schedule.dto.*;
import com.cuki.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;


    /**
     * 페이징 처리 -> 따로 레포 팔 것.
     */
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
    public IdResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        final Member currentMember = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final Schedule schedule =
                registrationRequestDto.of(currentMember, new DateTime(registrationRequestDto.getStartDateTime(), registrationRequestDto.getEndDateTime()), new Location(registrationRequestDto.getPlace())
                );

        return new IdResponseDto(schedulesRepository.save(schedule).getId());
    }

    // main
    public List<AllScheduleResponseDto> getAllSchedule() {
        final List<Schedule> allSchedule = schedulesRepository.findAll();

        // 최신 순으로 정렬하기  -> dto 에서 다시 작업
        allSchedule.sort(
                (a, b) -> b.getCreatedDate().compareTo(a.getCreatedDate())
        );

        List<AllScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : allSchedule) {
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
    public List<MyScheduleResponseDto> getMySchedule(Long memberId) {
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new IllegalArgumentException("요청한 데이터와 로그인한 회원 데이터가 일치하지 않습니다.");
        }

        final List<Schedule> allMySchedule = schedulesRepository.findAllByMemberId(SecurityUtil.getCurrentMemberId());

        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();
        for (Schedule schedule : allMySchedule) {
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
    public IdResponseDto deleteSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정 게시글 입니다.")
        );

        if (WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            schedulesRepository.delete(schedule);
        } else {
            throw new IllegalArgumentException("게시글 작성자만 삭제할 수 있습니다.");
        }

        return new IdResponseDto(schedule.getId());
    }
}
