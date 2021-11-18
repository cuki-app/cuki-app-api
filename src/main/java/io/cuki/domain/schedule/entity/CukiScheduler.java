package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class CukiScheduler {

    private final SchedulesRepository schedulesRepository;


    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void manageSchedule() {
        final LocalDate today = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        final LocalTime startTime = LocalTime.of(0, 0, 0, 0);
        final LocalTime endTime = LocalTime.of(23, 59, 59, 999_999_999);
        log.debug("오늘 날짜 = {}, 오늘 시간 = {} ~ {}", today, startTime, endTime);

        schedulesRepository
                .findByDateTime_EndDateTimeBetweenAndStatus(LocalDateTime.of(today, startTime), LocalDateTime.of(today, endTime), ScheduleStatus.IN_PROGRESS)
                .forEach(Schedule::updateStatusToDone);

        }

    }
