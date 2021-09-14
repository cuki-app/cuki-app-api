package com.cuki.repository;

import com.cuki.entity.Member;
import com.cuki.entity.Participation;
import com.cuki.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByMemberAndSchedule(Member member, Schedule schedule);

    findBy



}
