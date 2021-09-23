package com.cuki.domain.participation.service;

import com.cuki.domain.schedule.entity.ScheduleStatus;
import com.cuki.domain.schedule.utils.WriterVerification;
import com.cuki.domain.participation.entity.PermissionResult;
import com.cuki.domain.member.entity.Member;
import com.cuki.domain.participation.entity.Participation;
import com.cuki.domain.participation.dto.*;
import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.member.repository.MemberRepository;
import com.cuki.domain.participation.repository.ParticipationRepository;
import com.cuki.domain.schedule.repository.SchedulesRepository;
import com.cuki.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 해야할 것:
 * 1. 작성자가 '참여마감' 버튼 누른 경우 -> DONE
 * 2. 정원이 차면 자동으로 마감 -> DONE
 * 3. 종료 날짜가 되면 자동으로 마감 -> DONE
 * 4. 확정 유저 명단 -> DONE
 * 5. 참여 마감 -> 참여 신청 보내기 막기 -> DONE
 * 6. 참여 마감 -> 참여 신청 허가 | 거절  안되게 -> DONE
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipationService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;
    private final ParticipationRepository participationRepository;


    // 일정 요약 정보 보여주기
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

        if (schedule.isNotOverFixedNumber() && participation.getResult().equals(PermissionResult.NONE)) {
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
        throw new IllegalAccessException("모집인원 초과 || 참여 신청 결정은 한 번만 할 수 있습니다.");
    }

    public Set<ParticipantInfoResponseDto> getParticipantList(Long scheduleId) {
        Set<ParticipantInfoResponseDto> members = new HashSet<>();
        final Set<Participation> participationByAccept = participationRepository.findByScheduleIdAndResult(scheduleId, PermissionResult.ACCEPT);
        for (Participation participation : participationByAccept) {
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
            throw new IllegalArgumentException("이미 모집이 마감된 스케쥴 입니다.");
        }
    }
}
