package com.cuki.domain.participation.repository;

import com.cuki.domain.member.domain.Member;
import com.cuki.domain.participation.entity.Participation;
import com.cuki.domain.participation.entity.PermissionResult;
import com.cuki.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByMemberAndSchedule(Member member, Schedule schedule);

    List<Participation> findBySchedule(Schedule schedule);

    List<Participation> findByScheduleId(Long scheduleId);

    Set<Participation> findByScheduleIdAndResult(Long scheduleId, PermissionResult result);




}
