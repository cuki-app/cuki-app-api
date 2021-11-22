package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.exception.InvalidPeriodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Schedule Period 도메인 테스트")
class SchedulePeriodTest {

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    @DisplayName("시작일이 null 이면 InvalidPeriodException 예외가 발생한다.")
    void testNullStartDateTime() {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(3);
        log.info("testNullStartDateTime() - startDateTime = {}, endDateTime = {}", startDateTime, endDateTime);

        assertThrows(InvalidPeriodException.class,
                () -> new SchedulePeriod(startDateTime, endDateTime)
        );
    }

    @Test
    @DisplayName("종료일이 null 이면 InvalidPeriodException 예외가 발생한다.")
    void testNullEndDateTime() {
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(3);
        LocalDateTime endDateTime = null;
        log.info("testNullEndDateTime() - startDateTime = {}, endDateTime = {}", startDateTime, endDateTime);

        assertThrows(
                InvalidPeriodException.class,
                () -> new SchedulePeriod(startDateTime, endDateTime)
        );
    }

    @Test
    @DisplayName("시작일이 현재보다 이전일 경우 InvalidPeriodException 예외가 발생한다.")
    void testStartDateTimeIsBeforeNow() {
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);
        log.info("testStartDateTimeIsBeforeNow() - startDateTime = {}, endDateTime = {}", startDateTime, endDateTime);

        assertThrows(
                InvalidPeriodException.class,
                () -> new SchedulePeriod(startDateTime, endDateTime)
        );
    }

    @Test
    @DisplayName("종료일이 시작일보다 이전일 경우 InvalidPeriodException 예외가 발생한다.")
    void testEndDateTimeIsBeforeStartDateTime() {
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = startDateTime.minusDays(1);
        log.info("testEndDateTimeIsBeforeStartDateTime() - startDateTime = {}, endDateTime = {}", startDateTime, endDateTime);

        assertThrows(
                InvalidPeriodException.class,
                () -> new SchedulePeriod(startDateTime, endDateTime)
        );
    }
}