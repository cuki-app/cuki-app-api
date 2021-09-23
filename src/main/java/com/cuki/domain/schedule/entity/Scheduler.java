package com.cuki.domain.schedule.entity;

import com.cuki.domain.schedule.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final SchedulesRepository schedulesRepository;

    // 종료 날짜가 되면 자동으로 마감 -> 한 페이지에 나와있는 스케쥴 갯수만 검사한다면?
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void manageSchedule() {
        final List<Schedule> allByStatus = schedulesRepository.findAllByStatus(ScheduleStatus.IN_PROGRESS);
        for (Schedule schedule : allByStatus) {
            final LocalDateTime endDateTime = schedule.getDateTime().getEndDateTime();
            final LocalDate endTimeFromSchedule = LocalDate.of(endDateTime.getYear(), endDateTime.getMonthValue(), endDateTime.getDayOfMonth());
            if (endTimeFromSchedule.equals(LocalDate.now())) {
                log.info("end time LocalDate = {}", endTimeFromSchedule);
                log.info("현재 LocalDate = {}", LocalDate.now());
                schedule.updateStatus();
            }

        }

    }
}
