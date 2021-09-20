package com.cuki.domain.participation.repository;

import com.cuki.domain.member.entity.Member;
import com.cuki.domain.participation.domain.Participation;
import com.cuki.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByMemberAndSchedule(Member member, Schedule schedule);

    List<Participation> findBySchedule(Schedule schedule);

    List<Participation> findByScheduleId(Long scheduleId);




}
