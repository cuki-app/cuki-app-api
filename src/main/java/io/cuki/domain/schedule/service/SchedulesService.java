package io.cuki.domain.schedule.service;

import io.cuki.domain.participation.dto.ScheduleSummaryResponseDto;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.domain.schedule.utils.*;
import io.cuki.domain.schedule.repository.SchedulesRepository;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.global.util.SecurityUtil;
import io.cuki.domain.schedule.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;


    @Transactional
    public IdResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        final Schedule schedule = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(registrationRequestDto::toEntity)
                .orElseThrow(MemberNotFoundException::new);

        return new IdResponseDto(schedulesRepository.save(schedule).getId());
    }

    // main
    @Transactional(readOnly = true)
    public List<AllScheduleResponseDto> getAllSchedule() {
        List<AllScheduleResponseDto> responseDtoList = new ArrayList<>();

        schedulesRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedDate().compareTo(a.getCreatedDate()))
                .forEach(schedule -> responseDtoList.add(AllScheduleResponseDto.of(schedule)));

        return responseDtoList;
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        return schedulesRepository.findById(scheduleId)
                .map(OneScheduleResponseDto::of)
                .orElseThrow(ScheduleNotFoundException::new);
    }

    // 일정 요약 정보 보여주기
    @Transactional(readOnly = true)
    public ScheduleSummaryResponseDto getScheduleSummary(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정 게시글 입니다.")
        );

        return ScheduleSummaryResponseDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .place(schedule.getLocation().getPlace())
                .startDateTime(schedule.getDateTime().getStartDateTime())
                .endDateTime(schedule.getDateTime().getEndDateTime())
                .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                .numberOfPeopleWaiting(schedule.getNumberOfPeopleWaiting())
                .status(schedule.getStatus())
                .build();
    }


    // 내가 등록한 모집 일정 전체 보여주기
    @Transactional(readOnly = true)
    public List<MyScheduleResponseDto> getMySchedule(Long memberId) {
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new MemberNotMatchException("현재 로그인 한 회원과 파라미터의 회원 정보가 일치하지 않습니다.");
        }

        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();

        schedulesRepository.findAllByMemberId(SecurityUtil.getCurrentMemberId())
                .forEach(schedule -> responseDtoList.add(MyScheduleResponseDto.of(schedule)));

        return responseDtoList.stream().sorted().collect(Collectors.toList());

    }


    @Transactional
    public IdResponseDto deleteSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        if (WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            schedulesRepository.delete(schedule);
        } else {
            throw new MemberNotMatchException("현재 로그인 한 회원과 게시글 작성자가 일치하지 않습니다.");
        }

        return new IdResponseDto(schedule.getId());
    }


    @Transactional
    public IdAndStatusResponseDto closeUpSchedule(CloseUpScheduleRequestDto closeUpRequestDto) {
        final Schedule schedule = schedulesRepository.findById(closeUpRequestDto.getScheduleId()).orElseThrow(ScheduleNotFoundException::new);

        if (!WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            throw new MemberNotMatchException("현재 로그인 한 회원과 게시글 작성자가 일치하지 않습니다.");
        }

        if (schedule.getStatus() == ScheduleStatus.DONE) {
            throw new IllegalArgumentException("이미 신청 마감 처리 되었습니다.");
        }
        schedule.updateStatusToDone();
        return IdAndStatusResponseDto.of(schedule);
    }

}

