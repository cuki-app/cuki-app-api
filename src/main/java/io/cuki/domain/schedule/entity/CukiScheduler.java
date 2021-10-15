package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class CukiScheduler {

    private final SchedulesRepository schedulesRepository;

    // repo 에서 endDateTime 에 접근해야 하는데, query 를 날려서 접근 해야 할 것 같다.
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void manageSchedule() {
        schedulesRepository.findAllByStatus(ScheduleStatus.IN_PROGRESS)
                .forEach(schedule -> {
                    if (schedule.getDateTime().getEndDateTime().toLocalDate().equals(LocalDate.now())) {
                        log.info("end date = {}", schedule.getDateTime().getEndDateTime().toLocalDate());
                        log.info("현재 LocalDate = {}", LocalDate.now());
                        schedule.updateStatusToDone();
                    }
                });
        }

    }
