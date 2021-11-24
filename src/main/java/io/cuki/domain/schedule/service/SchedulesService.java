package io.cuki.domain.schedule.service;

import io.cuki.domain.schedule.entity.Location;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleAuthority;
import io.cuki.domain.schedule.entity.SchedulePeriod;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.domain.schedule.utils.*;
import io.cuki.domain.schedule.repository.SchedulesRepository;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.member.exception.MemberNotFoundException;
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


    @Transactional
    public IdResponseDto createSchedule(ScheduleRegistrationRequestDto registrationRequestDto) {
        final Schedule schedule = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(member -> Schedule.create(
                        registrationRequestDto.getTitle(),
                        member,
                        new SchedulePeriod(registrationRequestDto.getStartDateTime(), registrationRequestDto.getEndDateTime()),
                        registrationRequestDto.getFixedNumberOfPeople(),
                        new Location(registrationRequestDto.getPlace()),
                        registrationRequestDto.getDetails()))
                .orElseThrow(MemberNotFoundException::new);

//        final Schedule schedule = memberRepository.findById(SecurityUtil.getCurrentMemberId())
//                                            .map(registrationRequestDto::toEntity)
//                                            .orElseThrow(MemberNotFoundException::new);
        log.debug("모집 인원( 작성자 포함 ) = {}", schedule.getFixedNumberOfPeople());
        return new IdResponseDto(schedulesRepository.save(schedule).getId());
    }


    @Transactional(readOnly = true) //
    public SliceCustom<AllScheduleResponseDto> getAllSchedule(int page, int size) {
        log.info("client request - page number = {}, page size = {}", page, size);

        final PageRequest pageRequest = makePageRequest(page, size);
        List<AllScheduleResponseDto> dtoList = new ArrayList<>();
        final boolean hasNext = schedulesRepository.findBy(pageRequest).hasNext();
        schedulesRepository.findBy(pageRequest).forEach(schedule -> dtoList.add(AllScheduleResponseDto.of(schedule)));

        final Slice<AllScheduleResponseDto> dtoSlice = new SliceImpl<>(dtoList, pageRequest, hasNext);
        log.debug("schedule response dto is empty = {}", dtoSlice.isEmpty());
        log.debug("response - page number = {}", dtoSlice.getNumber());

        return new SliceCustom<>(dtoList, dtoSlice.hasNext(), dtoSlice.getNumber());
    }

    private PageRequest makePageRequest(int page, int size) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        return PageRequest.of(page, size, sort);
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public OneScheduleResponseDto getOneSchedule(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        if (WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            return OneScheduleResponseDto.of(schedule, ScheduleAuthority.OWNER);
        }

        return OneScheduleResponseDto.of(schedule, ScheduleAuthority.GUEST);
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

        WriterVerification.hasWriterAuthority(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId());
        schedulesRepository.delete(schedule);

        return new IdResponseDto(schedule.getId());
    }


    @Transactional
    public IdAndStatusResponseDto changeScheduleStatusToDone(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        WriterVerification.hasWriterAuthority(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId());
        schedule.statusIsNotDone();

        schedule.updateStatusToDone();
        log.debug("ScheduleService#changeScheduleStatusToDone - status = {}", schedule.getStatus());
        return IdAndStatusResponseDto.of(schedule);
    }

}

