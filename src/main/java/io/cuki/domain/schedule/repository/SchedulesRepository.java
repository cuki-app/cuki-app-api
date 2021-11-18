package io.cuki.domain.schedule.repository;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);

    Slice<Schedule> findBy(Pageable pageable);

    List<Schedule> findByDateTime_EndDateTimeBetweenAndStatus(LocalDateTime start, LocalDateTime end, ScheduleStatus status);
}
