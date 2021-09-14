package com.cuki.service;

import com.cuki.controller.dto.ApplyParticipationRequestDto;
import com.cuki.controller.dto.ParticipationSimpleResponseDto;
import com.cuki.controller.dto.ScheduleSummaryResponseDto;
import com.cuki.entity.Member;
import com.cuki.entity.Participation;
import com.cuki.entity.Schedule;
import com.cuki.repository.MemberRepository;
import com.cuki.repository.ParticipationRepository;
import com.cuki.repository.SchedulesRepository;
import com.cuki.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ParticipationService {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;
    private final ParticipationRepository participationRepository;


    public ParticipationSimpleResponseDto createParticipation(ApplyParticipationRequestDto requestDto) {
        final Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        final Schedule schedule = schedulesRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );
        final Participation participation = new Participation(member, schedule, requestDto.getReasonForParticipation());

        participationRepository.save(participation);

        return ParticipationSimpleResponseDto.builder()
                .scheduleId(schedule.getId())
                .participationId(participation.getId())
                .build();
    }
}
