package io.cuki.domain.participation.service;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.exception.MemberNotFoundException;
import io.cuki.domain.participation.exception.*;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.participation.dto.*;
import io.cuki.domain.schedule.exception.ScheduleNotFoundException;
import io.cuki.domain.schedule.exception.ScheduleStatusIsAlreadyChangedException;
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
    public ParticipationResultResponseDto createParticipation(Long scheduleId, ParticipationRegistrationRequestDto requestDto) throws DuplicateParticipationException, ScheduleStatusIsAlreadyChangedException, InappropriateAccessToParticipationException, ScheduleNotFoundException {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        final Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(MemberNotFoundException::new);

        if (WriterVerification.isWriter(requestDto.getMemberId(), schedule.getMember().getId())) {
            throw new InappropriateAccessToParticipationException("게시글 작성자는 참여 기능을 사용할 수 없습니다.");
        }

        if (participationRepository.findByMemberIdAndScheduleId(requestDto.getMemberId(), scheduleId).isPresent()) {
            throw new DuplicateParticipationException("중복 참여는 불가능합니다.");
        }

        if (!schedule.isNotOverFixedNumber()) {
            throw new FixedNumberOutOfBoundsException("정원이 초과되었습니다.");
        }

        schedule.statusIsNotDone();
        //
        final Participation participation = requestDto.toEntity(member, schedule);
        participationRepository.save(participation);
        schedule.updateNumberOfPeopleWaiting(participation.getResult());


        return ParticipationResultResponseDto.of(participation);
    }



    public Set<WaitingListInfoResponseDto> getWaitingList(Long scheduleId) throws WriterAuthorityException, ScheduleNotFoundException {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);
        if (!WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            throw new WriterAuthorityException("게시글 작성자만 확인할 수 있는 정보입니다.");
        }

        Set<WaitingListInfoResponseDto> responseDtoSet = new HashSet<>();
        participationRepository.findBySchedule(schedule).forEach(participation ->  responseDtoSet.add(WaitingListInfoResponseDto.of(participation)));

        return responseDtoSet;
    }


    // 테스트 후 삭제할 것
    // 참여 대기자 정보 보기 (작성자만)
    public WaitingDetailsInfoResponseDto getWaitingDetailsInfo(Long participationId) throws IllegalAccessException {
        Participation participation = participationRepository.findById(participationId).orElseThrow(
                () -> new IllegalArgumentException("참여 정보가 존재하지 않습니다.")
        );

        if (WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), participation.getSchedule().getMember().getId())) {
            return new WaitingDetailsInfoResponseDto(participation.getId(), participation.getMember().getNickname(), participation.getReasonForParticipation());
        } else {
            throw new IllegalAccessException("참여 대기자의 정보는 게시글 작성자만 볼 수 있습니다.");
        }

    }


    // 참여 수락 또는 거절하기
    @Transactional
    public PermissionResponseDto updatePermission(PermissionRequestDto permissionRequestDto) throws ParticipationNotFoundException, WriterAuthorityException, ScheduleStatusIsAlreadyChangedException {
        // requestDto - participation id, answer
        final Participation participation = participationRepository.findById(permissionRequestDto.getParticipationId()).orElseThrow(() -> new ParticipationNotFoundException("참여 정보가 존재하지 않습니다."));
        final Schedule schedule = participation.getSchedule();

        // 반환값이 전부 true 가 나와야 비즈니스 로직으로 넘어간다.
        WriterVerification.isCheckedAsTheWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId());
        schedule.isNotOverFixedNumber();
        schedule.statusIsNotDone();
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


    public Set<ParticipantInfoResponseDto> getParticipantList(Long scheduleId) {
        // 확정자 명단 조회는 누구나 조회 가능한지?
        Set<ParticipantInfoResponseDto> members = new HashSet<>();
        final Set<Participation> participantsByAccept = participationRepository.findByScheduleIdAndResult(scheduleId, PermissionResult.ACCEPT);
        for (Participation participation : participantsByAccept) {
            members.add(ParticipantInfoResponseDto.of(participation));
        }
        return members;
    }

    @Transactional
    public List<MyParticipationResponseDto> getMyParticipationList(Long memberId) {
        List<MyParticipationResponseDto> responseDtoList = new ArrayList<>();

        participationRepository.findByMemberId(memberId)
                .forEach(participation -> responseDtoList.add(MyParticipationResponseDto.of(participation)));

        Collections.sort(responseDtoList);

        return responseDtoList;
    }


    // 내가 참여한 게시글 상세 조회
    // 스케쥴 상세조회 (스케쥴에 정의된 API)+ 참여한 스케쥴 상세조회 (this)
    @Transactional
    public OneParticipationResponseDto getOneParticipation(Long memberId, Long participationId) throws ParticipationNotFoundException {
        // exception 처리 해야 함
        final Participation participation = participationRepository.findById(participationId).orElseThrow(() -> new ParticipationNotFoundException("참여 정보가 존재하지 않습니다."));
        // participation id 와 member id 가 일치해야 볼 수 있다.
        // member id = 2(작성자) 가 participation id 를 알고 uri 치고 들어오면 보임
        return OneParticipationResponseDto.of(participation);
    }
}



