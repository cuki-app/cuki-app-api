package com.cuki.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Schedule extends BaseTimeEntity{
    /**
     *  1. 날짜 구간 (시작일 ~ 종료일)
     *  2. 시간대 설정 (시작 시간 ~ 종료 시간)
     *  2.1 default = 종일
     *  3. 인원
     *  4. 장소 입력 (검색 엔진 + map)
     *  5. 설명
     *  * 1 ~ 5 필수 입력 사항
     *  * timestamp
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private boolean allDay = true;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @NotNull
    private int participants;   // max 조정

    private String location;

    private int locationX;

    private int locationY;

    @NotNull
    private String description;



}
