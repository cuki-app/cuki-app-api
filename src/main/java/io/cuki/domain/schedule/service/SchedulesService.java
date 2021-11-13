package io.cuki.domain.schedule.service;

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
import io.cuki.global.util.SliceCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulesService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;



    @Transactional(readOnly = true) //
    public SliceCustom<AllScheduleResponseDto> getAllSchedule(Pageable pageable) {
        log.debug("client request - page number = {}, page size = {}", pageable.getPageNumber(), pageable.getPageSize());
        final Sort createdDate = Sort.by(Sort.Direction.DESC, "createdDate");
        final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), createdDate);

        final Slice<Schedule> scheduleSlice = schedulesRepository.findBy(pageRequest);
        List<AllScheduleResponseDto> dtoList = new ArrayList<>();
        for (Schedule schedule : scheduleSlice) {
            log.debug("repo 에서 뽑은 schedule id = {}", schedule.getId());
            dtoList.add(AllScheduleResponseDto.of(schedule));
        }

        final Slice<AllScheduleResponseDto> dtoSlice = new SliceImpl<>(dtoList, pageRequest, scheduleSlice.hasNext());
        log.debug("schedule response dto is empty ? = {}", dtoSlice.isEmpty());
        log.debug("response - page number = {}", dtoSlice.getNumber());

        return new SliceCustom<>(dtoList, dtoSlice.hasNext(), dtoSlice.getNumber());
    }

    @Transactional
    public IdResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        log.debug("registrationRequestDto = {}", registrationRequestDto);

        final Schedule schedule = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(registrationRequestDto::toEntity)
                .orElseThrow(MemberNotFoundException::new);

        return new IdResponseDto(schedulesRepository.save(schedule).getId());
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        return schedulesRepository.findById(scheduleId)
                .map(OneScheduleResponseDto::of)
                .orElseThrow(ScheduleNotFoundException::new);
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
            log.debug("로그인 한 회원 = {}, 게시글 작성자 = {}", SecurityUtil.getCurrentMemberId(), schedule.getId());
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
            log.debug("{}번 게시글은 이미 마감 처리되었습니다.", closeUpRequestDto.getScheduleId());
            throw new IllegalArgumentException("이미 신청 마감 처리 되었습니다.");
        }
        schedule.updateStatusToDone();
        return IdAndStatusResponseDto.of(schedule);
    }

}

