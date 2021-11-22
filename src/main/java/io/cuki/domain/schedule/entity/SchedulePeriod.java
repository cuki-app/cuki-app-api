package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.exception.InvalidPeriodException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Slf4j
@Getter
@NoArgsConstructor
@Embeddable
public class SchedulePeriod {

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;


    public SchedulePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        checkPeriodValidation(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void checkPeriodValidation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null) {
            log.error("시작일 = {}", startDateTime);
            throw new InvalidPeriodException("시작일을 입력해주세요.");
        }

        if (endDateTime == null) {
            log.error("종료일 = {}", endDateTime);
            throw new InvalidPeriodException("종료일을 입력해주세요.");
        }

        if (startDateTime.isBefore(LocalDateTime.now())) {
            log.error("시작일 {} 이 현재일 {} 보다 이전 입니다.", startDateTime, LocalDateTime.now());
            throw new InvalidPeriodException("시작일(" + startDateTime + ")은 현재 시간보다 이전일 수 없습니다.");
        }

        if (startDateTime.compareTo(endDateTime) > 0) {
            log.error("시작일 {} 이 종료일 {} 보다 늦습니다.", startDateTime, endDateTime);
            throw new InvalidPeriodException("시작일(" + startDateTime + ")이 종료일(" + endDateTime + " )보다 늦습니다.");
        }
    }


}
