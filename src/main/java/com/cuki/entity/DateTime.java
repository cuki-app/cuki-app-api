package com.cuki.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@Entity
public class DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dateTime_id")
    private Long id;

    private boolean allDay = true;

    private LocalDateTime startDateTime;    // 2021.08.20.12:34:30

    private LocalDateTime endDateTime;

}
