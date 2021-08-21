package com.cuki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class ScheduleResponseDto implements Comparable<ScheduleResponseDto> {

    private Long id;

    private String title;

    private boolean allDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    private LocalDateTime createdDate;

    private int participants;

    private String place;

    private String description;

    /**
     * 작성일자 최신순으로 정렬하기
     */
    @Override
    public int compareTo(ScheduleResponseDto o) {
        return createdDate.compareTo(o.getCreatedDate());
    }
}
