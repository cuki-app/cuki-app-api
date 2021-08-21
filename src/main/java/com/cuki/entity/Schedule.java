package com.cuki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

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
    @Column(name = "id")
    private Long id;

    @Column(name= "title", length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * save the transient instance before flushing
     * 개체가 저장되지 않은 일시적인 인스턴스를 참조합니다. 플러싱하기 전에 임시 인스턴스를 저장합니다.
     * @OneToMany나 @ManyToOne 사용 시 부모 객체에 추가하는 자식 객체가 아직 db에 저장되지 않아 생긴 에러이다. 즉 영속성 전이를 해야한다.
     */
    @OneToOne(cascade = CascadeType.ALL)
    private DateTime dateTime;


    /**
     * 객체 분리
     * 1. max 설정
     * 2. 현재 인원 설정 (count)
     * 3. 현재 상태 설정
     */
    @Column(name = "participants")
    private int participants;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;


    @Column(name = "description", length = 300)
    private String description;


}
