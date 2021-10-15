<<<<<<< HEAD:src/main/java/io/cuki/domain/member/schedule/repository/SchedulesRepository.java
package com.cuki.schedule.repository;

import com.cuki.schedule.domain.Schedule;
=======
package io.cuki.domain.schedule.repository;

import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleStatus;
>>>>>>> cce819170e3d106b98658538837bd3b03063fbcc:src/main/java/io/cuki/domain/schedule/repository/SchedulesRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);

    List<Schedule> findAllByStatus(ScheduleStatus status);

}
