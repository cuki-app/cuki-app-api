package com.cuki.repository;

import com.cuki.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByUserId(Long userId);



}
