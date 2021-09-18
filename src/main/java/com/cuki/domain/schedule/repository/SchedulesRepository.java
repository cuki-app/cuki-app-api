package com.cuki.domain.schedule.repository;

<<<<<<< HEAD:src/main/java/com/cuki/schedule/repository/SchedulesRepository.java
import com.cuki.schedule.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
import com.cuki.domain.schedule.domain.Schedule;
>>>>>>> 0e5c631fe1bfe81b7f2cdb764ea9d5b757e48cd1:src/main/java/com/cuki/domain/schedule/repository/SchedulesRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);

    Page<Schedule> findAllByMemberNickname(String nickname, Pageable pageable);



}
