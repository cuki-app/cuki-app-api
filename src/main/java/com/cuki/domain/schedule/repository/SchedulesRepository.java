package com.cuki.domain.schedule.repository;

import com.cuki.domain.schedule.entity.ScheduleStatus;
import com.cuki.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);

    List<Schedule> findAllByStatus(ScheduleStatus status);

}
