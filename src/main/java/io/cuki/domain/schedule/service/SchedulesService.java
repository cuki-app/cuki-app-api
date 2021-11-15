package io.cuki.domain.schedule.service;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleAuthority;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.domain.schedule.exception.ScheduleStatusIsAlreadyChangedException;
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

import java.nio.file.AccessDeniedException;
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
        log.debug("모집 인원(작성자 포함) = {}", schedule.getFixedNumberOfPeople());

        return new IdResponseDto(schedulesRepository.save(schedule).getId());
    }

    @Transactional(readOnly = true) //
    public SliceCustom<AllScheduleResponseDto> getAllSchedule(int page, int size) {
        log.debug("client request - page number = {}, page size = {}", page, size);
        final Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        final PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<AllScheduleResponseDto> dtoList = new ArrayList<>();
        final boolean hasNext = schedulesRepository.findBy(pageRequest).hasNext();
        schedulesRepository.findBy(pageRequest).forEach(schedule -> dtoList.add(AllScheduleResponseDto.of(schedule)));

        final Slice<AllScheduleResponseDto> dtoSlice = new SliceImpl<>(dtoList, pageRequest, hasNext);
        log.debug("schedule response dto is empty = {}", dtoSlice.isEmpty());
        log.debug("response - page number = {}", dtoSlice.getNumber());

        return new SliceCustom<>(dtoList, dtoSlice.hasNext(), dtoSlice.getNumber());
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        if (!SecurityUtil.getCurrentMemberId().equals(schedule.getMember().getId())) {
            return OneScheduleResponseDto.of(schedule, ScheduleAuthority.GUEST);
        } else {
            return OneScheduleResponseDto.of(schedule, ScheduleAuthority.OWNER);
        }
    }



    // 내가 등록한 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<MyScheduleResponseDto> getMySchedule(Long writerId) {
        List<MyScheduleResponseDto> responseDtoList = new ArrayList<>();

        schedulesRepository.findAllByMemberId(writerId)
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
            throw new MemberNotMatchException("게시글 삭제는 게시글 작성자만 할 수 있습니다.");
        }

        return new IdResponseDto(schedule.getId());
    }


    @Transactional
    public IdAndStatusResponseDto changeScheduleStatus(Long scheduleId) throws ScheduleStatusIsAlreadyChangedException {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        if (!WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            throw new MemberNotMatchException("게시글 마감은 게시글 작성자만 할 수 있습니다.");
        }

        if (schedule.getStatus() == ScheduleStatus.DONE) {
            log.debug("{}번 게시글은 이미 마감 처리되었습니다.", scheduleId);
            throw new ScheduleStatusIsAlreadyChangedException("이미 마감 처리된 게시글 입니다.");
        }
        schedule.updateStatusToDone();
        return IdAndStatusResponseDto.of(schedule);
    }


}

