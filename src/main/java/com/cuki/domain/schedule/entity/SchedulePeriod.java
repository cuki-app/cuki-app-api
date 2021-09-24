package com.cuki.domain.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SchedulePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;


    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;


    public SchedulePeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        checkPeriodValidation(startDateTime, endDateTime);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void checkPeriodValidation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (Objects.isNull(startDateTime)) {
            throw new IllegalArgumentException("시작일은 null 일 수 없습니다.");
        }

        if (Objects.isNull(endDateTime)) {
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
