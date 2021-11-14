package io.cuki.domain.participation.service;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import io.cuki.domain.participation.dto.*;
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


    // 마감된 모집 일정에 참여 신청 할 수 없게
    @Transactional
    public ParticipationSimpleResponseDto createParticipation(ApplyParticipationRequestDto requestDto) throws IllegalAccessException {
        final Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final Schedule schedule = schedulesRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );

        isDuplicateParticipation(member, schedule);
        isAlreadyCompleted(schedule);

        // 작성자이면 안되고, 모집 인원 초과가 아니어야 한다. -> '모집 마감' 상태가 아니어야 한다.
        if (!WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            if (schedule.isNotOverFixedNumber()) {
                final Participation participation = new Participation(member, schedule, requestDto.getReasonForParticipation());
                participationRepository.save(participation);

                schedule.updateNumberOfPeopleWaiting(PermissionResult.NONE);

                return ParticipationSimpleResponseDto.builder()
                        .scheduleId(schedule.getId())
                        .participationId(participation.getId())
                        .build();
            } else {
                throw new IllegalAccessException("모집이 마감되었습니다.");
            }

        } else {
            throw new IllegalAccessException("게시글 작성자는 본인의 모집 일정에 참여하기 기능을 사용할 수 없습니다.");
        }

    }


    // 참여 대기 확인하기 (작성자만)
    public Set<WaitingInfoResponseDto> getWaitingList(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다."));
        Set<WaitingInfoResponseDto> members = new HashSet<>();

        if (WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            final List<Participation> participationList = participationRepository.findByScheduleId(scheduleId); // set 기준 ?

            for (Participation participation : participationList) {
                members.add(WaitingInfoResponseDto.of(participation));
            }

        } else {
            throw new IllegalArgumentException("게시글 작성자만 볼 수 있는 정보입니다.");
        }

        return members;
    }


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


    @Transactional
    public PermissionResponseDto updatePermission(PermissionRequestDto permissionRequestDto) throws IllegalAccessException {
        final Participation participation = participationRepository.findById(permissionRequestDto.getParticipationId()).orElseThrow(
                () -> new IllegalArgumentException("참여 정보가 존재하지 않습니다.")
        );
        final Schedule schedule = participation.getSchedule();

        isAlreadyCompleted(schedule);

        if (!WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            throw new IllegalAccessException("해당 기능은 작성자만 이용할 수 있습니다.");
        }

        if (schedule.isNotOverFixedNumber() && participation.getResult() == PermissionResult.NONE) {
            if (permissionRequestDto.isAnswer()) {
                participation.updateResult(PermissionResult.ACCEPT);
                schedule.updateCurrentNumberOfPeople();
                schedule.updateNumberOfPeopleWaiting(PermissionResult.ACCEPT);
                return new PermissionResponseDto(participation.getId(), PermissionResult.ACCEPT);
            }
            // false
            participation.updateResult(PermissionResult.REJECT);
            schedule.updateNumberOfPeopleWaiting(PermissionResult.REJECT);
            return new PermissionResponseDto(participation.getId(), PermissionResult.REJECT);
        }
        // 분기 나눠서 각각의 예외 던질 것
        throw new IllegalAccessException("모집인원 초과 || 참여 신청 결정은 한 번만 할 수 있습니다.");
    }

    // 내가 참여한 게시글 상세 조회
    public ParticipationResponseDto getOneParticipation(Long memberId) {
        final List<Participation> participationList = participationRepository.findByMemberId(memberId);
        // 게시글 정보, 내 신청 정보

        return null;
    }

    public Set<ParticipantInfoResponseDto> getParticipantList(Long scheduleId) {
        // 확정자 명단 조회는 누구나 조회 가능한지?
        Set<ParticipantInfoResponseDto> members = new HashSet<>();
        final Set<Participation> participantsByAccept = participationRepository.findByScheduleIdAndResult(scheduleId, PermissionResult.ACCEPT);
        for (Participation participation : participantsByAccept) {
            members.add(ParticipantInfoResponseDto.of(participation));
        }
        return  members;
    }

    private void isDuplicateParticipation(Member member, Schedule schedule) {   // participation
        if (participationRepository.findByMemberAndSchedule(member, schedule).isPresent()) {
            throw new IllegalArgumentException("중복 참여는 불가능 합니다.");
        }
    }

    private void isAlreadyCompleted(Schedule schedule) {    // status - in Schedule
        if (schedule.getStatus().equals(ScheduleStatus.DONE)) {
            log.debug("스케쥴 상태 = {}", schedule.getStatus());
            throw new IllegalArgumentException("이미 모집이 마감된 스케쥴 입니다.");
        }
    }

    @Transactional
    public List<ParticipationResponseDto> getMyParticipation(Long memberId) {
        List<ParticipationResponseDto> responseDtoList = new ArrayList<>();
        // ParticipationResponseDto
        // OneParticipationResponseDto

        participationRepository.findByMemberId(memberId)
                .forEach(participation -> responseDtoList.add(ParticipationResponseDto.of(participation)));

        Collections.sort(responseDtoList);
        for (ParticipationResponseDto my : responseDtoList) {
            log.debug("response sort = {}", my.getStartDateTime());
        }

        return responseDtoList;
    }
}
