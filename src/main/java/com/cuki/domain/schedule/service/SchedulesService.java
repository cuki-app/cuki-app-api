package com.cuki.domain.schedule.service;


import com.cuki.domain.schedule.entity.SchedulePeriod;
import com.cuki.domain.schedule.entity.ScheduleStatus;
import com.cuki.domain.schedule.utils.WriterVerification;
import com.cuki.domain.member.entity.Member;
import com.cuki.domain.schedule.repository.SchedulesRepository;
import com.cuki.domain.member.repository.MemberRepository;
import com.cuki.domain.schedule.entity.Location;
import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.dto.*;
import com.cuki.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public AllScheduleWithSliceResponseDto getAllSchedulesWithSlice(Pageable pageable) {
        final Slice<Schedule> schedulesWithSlice = schedulesRepository.findBy(pageable);

        List<AllScheduleResponseDto> responseDtoList = new ArrayList<>();

        for (Schedule schedule : schedulesWithSlice) {
            responseDtoList.add(
                    AllScheduleResponseDto.builder()
                            .scheduleId(schedule.getId())
                            .title(schedule.getTitle())
                            .nickname(schedule.getMember().getNickname())
                            .place(schedule.getLocation().getPlace())
                            .startDateTime(schedule.getDateTime().getStartDateTime())
                            .endDateTime(schedule.getDateTime().getEndDateTime())
                            .fixedNumberOfPeople(schedule.getFixedNumberOfPeople())
                            .currentNumberOfPeople(schedule.getCurrentNumberOfPeople())
                            .status(schedule.getStatus())
                            .build());
        }


        return AllScheduleWithSliceResponseDto.of(responseDtoList, schedulesWithSlice.hasNext());

    }


    public Page<Schedule> getScheduleByQueryMethod(Pageable pageable) {

        final Page<Schedule> allByMemberNickname = schedulesRepository.findAllByMemberNickname("무적토마토", pageable);

        for (Schedule schedule : allByMemberNickname) {
            log.info("닉네임으로 찾은 스케쥴 - 닉네임= {}", schedule.getMember().getNickname());
            log.info("닉네임으로 찾은 스케쥴 - 스케쥴 아이디 = {}", schedule.getId());
        }

        return schedulesRepository.findAllByMemberNickname("납작한감자", pageable);
    }




    @Transactional
    public IdResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        final Schedule schedule = memberRepository.findById(SecurityUtil.getCurrentMemberId()).map(registrationRequestDto::toEntity)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));

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

    // 일정 상세 조회  // SRP
    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        return schedulesRepository.findById(scheduleId).map(OneScheduleResponseDto::of).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 모집 일정글 입니다."));
    }


    // 내가 등록한 모집 일정 전체 보여주기
    public List<MyScheduleResponseDto> getMySchedule(Long memberId) {
        log.info("start time = {}", LocalDateTime.now());
        if (!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new IllegalArgumentException("현재 로그인한 회원과 데이터를 요청한 회원의 정보가 일치하지 않습니다.");
        }

        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();

        schedulesRepository.findAllByMemberId(SecurityUtil.getCurrentMemberId())
                .forEach(schedule -> responseDtoList.add(MyScheduleResponseDto.of(schedule)));

        return responseDtoList.stream().sorted().collect(Collectors.toList());

    }


    // Here !!
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

    // effective java
    @Transactional
    public IdAndStatusResponseDto updateStatus(UpdateStatusRequestDto statusRequestDto) {
        final Schedule schedule = schedulesRepository.findById(statusRequestDto.getScheduleId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케쥴 입니다."));
        if (schedule.getStatus().equals(ScheduleStatus.DONE)) {
            throw new IllegalArgumentException("이미 신청 마감 처리 되었습니다.");
        }
        schedule.updateStatus();
        return IdAndStatusResponseDto.of(schedule);
    }
}
