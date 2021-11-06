package io.cuki.domain.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SchedulePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime startDateTime;
    
    private LocalDateTime endDateTime;


    public SchedulePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        checkPeriodValidation(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void checkPeriodValidation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null) {
            throw new IllegalArgumentException("시작일은 null 일 수 없습니다.");
        }

        if (endDateTime == null) {
            throw new IllegalArgumentException("종료일은 null 일 수 없습니다.");
        }

        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("시작일(" + startDateTime + ")은 현재 시간보다 이전일 수 없습니다.");
        }

        if (startDateTime.compareTo(endDateTime) > 0) {
            throw new IllegalArgumentException("시작일(" + startDateTime + ")이 종료일(" + endDateTime + " )보다 늦습니다.");
        }
    }


}
