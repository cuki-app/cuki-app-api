package io.cuki.domain.participation.repository;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.participation.entity.Participation;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.participation.entity.PermissionResult;
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
