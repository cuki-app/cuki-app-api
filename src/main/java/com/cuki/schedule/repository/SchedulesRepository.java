package com.cuki.schedule.repository;

import com.cuki.schedule.domain.Location;
import com.cuki.schedule.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByMemberId(Long memberId);

//    Page<Schedule> findByMemberNickname(String nickname, Pageable pageable);

    Page<Schedule> findByMemberNickname(String nickname, Pageable pageable);

    Page<Schedule> findAllByMemberNickname(String nickname, Pageable pageable);



}
