package com.cuki.domain.schedule.repository;

import com.cuki.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);



}
