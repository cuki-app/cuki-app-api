package com.cuki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class AllScheduleResponseDto implements Comparable<AllScheduleResponseDto> {

    private Long id;

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    // 최신 순으로 정렬하기 위함 - builder하면 null. 어떻게 해결 할 것인가?
    // responseDto 의 책임에 필요하지 않은 데이터
    private LocalDateTime createdDate;

    private int participants;

    private String place;

    private String description;

    @Override
    public int compareTo(AllScheduleResponseDto o) {
        return createdDate.compareTo(o.getCreatedDate());
    }
}
