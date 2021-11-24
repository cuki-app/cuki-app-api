package io.cuki.domain.participation.service;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.member.exception.MemberNotMatchException;
import io.cuki.domain.participation.exception.*;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.participation.dto.*;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.domain.schedule.utils.WriterVerification;
import io.cuki.domain.participation.entity.PermissionResult;
import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.participation.repository.ParticipationRepository;
import io.cuki.domain.schedule.repository.SchedulesRepository;
import io.cuki.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipationService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;
    private final ParticipationRepository participationRepository;


    @Transactional
    public ParticipationResultResponseDto createParticipation(Long scheduleId, ParticipationRegistrationRequestDto requestDto) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        final Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(MemberNotFoundException::new);

        if (WriterVerification.isWriter(member.getId(), schedule.getMember().getId())) {
            throw new InappropriateAccessToParticipationException("게시글 작성자는 참여 기능을 사용할 수 없습니다.");
        }
        if (participationRepository.findByMemberAndSchedule(member, schedule).isPresent()) {
            throw new DuplicateParticipationException("중복 참여는 불가능합니다.");
        }
        schedule.checkScheduleConditionForParticipation();

//        schedule.isNotOverFixedNumber();
//        schedule.statusIsNotDone();

//        final Participation participation = requestDto.toEntity(member, schedule);
        final Participation participation = Participation.create(member, schedule, requestDto.getReasonForParticipation());

        participationRepository.save(participation);
        schedule.updateNumberOfPeopleWaiting(participation.getResult());

        return ParticipationResultResponseDto.of(participation);
    }

    @Transactional
    public List<MyParticipationResponseDto> getMyParticipationList(Long memberId) {
        List<MyParticipationResponseDto> responseDtoList = new ArrayList<>();

        participationRepository.findByMemberId(memberId)
                .forEach(participation -> responseDtoList.add(MyParticipationResponseDto.of(participation)));

        Collections.sort(responseDtoList);

        return responseDtoList;
    }


    // 스케쥴 상세조회 (스케쥴에 정의된 API)+ 참여한 스케쥴 상세조회 (this)
    @Transactional
    public OneParticipationResponseDto getOneParticipation(Long memberId, Long participationId) {
        final Participation participation = participationRepository.findById(participationId).orElseThrow(() -> new ParticipationNotFoundException("참여 정보가 존재하지 않습니다."));
        if (!participation.getMember().getId().equals(memberId)) {
            throw new MemberNotMatchException("참여자에 관한 정보는 참여 당사자만 확인할 수 있습니다.");
        }
        return OneParticipationResponseDto.of(participation);
    }


    // 참여 수락 또는 거절하기
    @Transactional
    public PermissionResponseDto updatePermission(PermissionRequestDto permissionRequestDto) {
        final Participation participation = participationRepository.findById(permissionRequestDto.getParticipationId()).orElseThrow(() -> new ParticipationNotFoundException("참여 정보가 존재하지 않습니다."));
        final Schedule schedule = participation.getSchedule();

        // 반환값이 전부 true 가 나와야 비즈니스 로직으로 넘어간다.
        WriterVerification.hasWriterAuthority(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId());
        schedule.checkScheduleConditionForParticipation();
//        schedule.isNotOverFixedNumber();
//        schedule.statusIsNotDone();
        // permission 이 true 인지 false 인지에 따라 비즈니스를 짜자.
        if (permissionRequestDto.isAnswer()) {
            participation.updateResult(PermissionResult.ACCEPT);
            log.debug("ParticipationService #updatePermission(): permission result - ACCEPT.");
            // 아무데서나 이 메소드를 남용하면???
            schedule.updateNumberOfPeopleWaiting(PermissionResult.ACCEPT);
        } else {
            participation.updateResult(PermissionResult.REJECT);
            log.debug("ParticipationService #updatePermission(): permission result - REJECT.");
            schedule.updateNumberOfPeopleWaiting(PermissionResult.REJECT);
        }
        return PermissionResponseDto.of(participation);
    }


    public Set<WaitingListInfoResponseDto> getWaitingList(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        WriterVerification.hasWriterAuthority(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId());
        Set<WaitingListInfoResponseDto> responseDtoSet = new HashSet<>();

        participationRepository.findByScheduleIdAndResult(scheduleId, PermissionResult.NONE)
                .forEach(participation ->  responseDtoSet.add(WaitingListInfoResponseDto.of(participation)));

        return responseDtoSet;
    }


    // 닉네임 매핑할 때 persistence 문제 생길텐데? -> 왜 안생기지?
    public Set<ParticipantInfoResponseDto> getParticipantList(Long scheduleId) {
        Set<ParticipantInfoResponseDto> participantInfoResponseDtoSet = new HashSet<>();

        participationRepository.findByScheduleIdAndResult(scheduleId, PermissionResult.ACCEPT)
                .forEach(participation -> participantInfoResponseDtoSet.add(ParticipantInfoResponseDto.of(participation)));
        return participantInfoResponseDtoSet;
    }
}