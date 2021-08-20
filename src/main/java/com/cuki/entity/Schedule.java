package com.cuki.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
@Getter
@Builder
@NoArgsConstructor
@Entity
public class Schedule extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "dateTime_id")   // 단방향으로
    private DateTime dateTime;


    /**
     * 1. max 설정
     * 2. 현재 인원 설정 (count)
     */
    @NotNull
    private int participants;

    private String location;

    private int locationX;

    private int locationY;

    @NotNull
    @Column(length = 300)
    private String description;


}
