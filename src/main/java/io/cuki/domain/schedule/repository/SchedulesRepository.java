package io.cuki.domain.schedule.repository;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.SchedulePeriod;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);

    List<Schedule> findAllByStatus(ScheduleStatus status);

    Slice<Schedule> findBy(Pageable pageable);

//    List<Schedule> findAllByDateTimeAndStatus(SchedulePeriod now, ScheduleStatus status);

    List<Schedule> findByDateTime_EndDateTimeDate(LocalDate today);
}
