package com.cuki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * @Getter 안붙이니 response 내려줄 때 500 에러 생긴다.
 */
@Builder
@AllArgsConstructor
@Getter
public class OneScheduleResponseDto {

    private Long id;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private int participants;

    private String place;

    private String description;


}
