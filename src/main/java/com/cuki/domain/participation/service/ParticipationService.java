package com.cuki.domain.participation.service;

import com.cuki.domain.schedule.utils.WriterVerification;
import com.cuki.domain.participation.domain.PermissionResult;
import com.cuki.domain.member.domain.Member;
import com.cuki.domain.participation.domain.Participation;
import com.cuki.domain.participation.dto.*;
import com.cuki.domain.schedule.domain.Schedule;
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
 * 1. 작성자가 '참여마감' 버튼 누른 경우
 * 2. 확정 유저 명단
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
                .build();
    }

    /**
     * 1. 멀티스레드 -> 동기화 문제 어떻게 할 것인지?
     * 2. 변경 가능성 있는 로직 -> 정책 변경: 인원이 모집 되지 않아도 작성자가 신청 마감 시킬 수 있다.
     * @param requestDto
     * @return
     */
    @Transactional
    public ParticipationSimpleResponseDto createParticipation(ApplyParticipationRequestDto requestDto) throws IllegalAccessException {
        final Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final Schedule schedule = schedulesRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );

        // 중복 참여 금지
        if (isDuplicateParticipation(member, schedule)) {
            throw new IllegalAccessException("중복 참여는 불가능합니다.");
        }

        // 작성자이면 안되고, 모집 인원 초과가 아니어야 한다.
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
    public ScheduleSummaryAndWaitingListResponseDto getWaitingList(Long scheduleId) {
        final Schedule schedule = schedulesRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다."));
        Set<WaitingInfoResponseDto> members = new HashSet<>();

        if (WriterVerification.isWriter(SecurityUtil.getCurrentMemberId(), schedule.getMember().getId())) {
            final List<Participation> participationList = participationRepository.findByScheduleId(scheduleId);

            for (Participation participation : participationList) {
                members.add(new WaitingInfoResponseDto(participation.getId(), participation.getMember().getNickname()));
            }

        } else {
            throw new IllegalArgumentException("게시글 작성자만 볼 수 있는 정보입니다.");
        }

        return new ScheduleSummaryAndWaitingListResponseDto(getScheduleSummary(scheduleId), members);
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
    public PermissionResponseDto decidePermission(PermissionRequestDto permissionRequestDto) throws IllegalAccessException {
        final Participation participation = participationRepository.findById(permissionRequestDto.getParticipationId()).orElseThrow(
                () -> new IllegalArgumentException("참여 정보가 존재하지 않습니다.")
        );
        final Schedule schedule = participation.getSchedule();

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

    private boolean isDuplicateParticipation(Member member, Schedule schedule) {
        return participationRepository.findByMemberAndSchedule(member, schedule).isPresent();
    }
}
