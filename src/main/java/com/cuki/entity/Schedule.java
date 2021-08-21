package com.cuki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *  1. 날짜 구간 (시작일 ~ 종료일)
 *  2. 시간대 설정 (시작 시간 ~ 종료 시간)
 *  2.1 default = 종일
 *  3. 인원
 *  4. 장소 입력 (검색 엔진 + map)
 *  5. 설명
 *  * 1 ~ 5 필수 입력 사항
 *  * timestamp
 *  * 부모 클래스를 만들고 1 개인, 2 모집 상속 받아서 쓸 수 없나?
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @NotNull
    @Column(length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_tomato_id")
    private User user;

    /**
     * save the transient instance before flushing
     * 개체가 저장되지 않은 일시적인 인스턴스를 참조합니다. 플러싱하기 전에 임시 인스턴스를 저장합니다.
     * @OneToMany나 @ManyToOne 사용 시 부모 객체에 추가하는 자식 객체가 아직 db에 저장되지 않아 생긴 에러이다. 즉 영속성 전이를 해야한다.
     */
    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "date_time_id")   // 단방향으로
    private DateTime dateTime;


    /**
     * 1. max 설정
     * 2. 현재 인원 설정 (count)
     */
    @NotNull
    private int participants;

//    @OneToMany(mappedBy = "schedule")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    @Column(length = 300)
    private String description;


}
