package io.cuki.entity;

import io.cuki.domain.schedule.entity.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@Disabled
@DisplayName("일정 등록 테스트")
class ScheduleTest {

    /**
     * action: 필수 정보는 모두 존재해야 한다 (제목, 날짜, 인원, 위치, 세부설명)
     * data : 제목, 날짜, 인원, 위치, 세부설명
     * to do: 멤버, 현재 모집 인원
     */

    @Test
    @DisplayName("제목이 null인 경우 예외가 발생한다.")
    void 제목이_없는_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title(null)
                        .build()
        );

    }

    @Test
    @DisplayName("제목이 공백인 경우 예외가 발생한다.")
    void 제목이_공백인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title("")
                        .build()
        );
    }

    @Test
    @DisplayName("시작일이 null인 경우 예외가 발생한다.")
    void 시작일이_null인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .startDateTime(null)
                        .build()
        );
    }

    @Test
    @DisplayName("종료일이 null인 경우 예외가 발생한다.")
    void 종료일이_null인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .endDateTime(null)
                        .build()
        );
    }

    @Test
    @DisplayName("시작일이 오늘 날짜인 경우 예외가 발생하면 안된다.")
    void 시작일이_오늘인_경우() {
        ScheduleFixtureBuilder.builder()
                .startDateTime(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("시작일이 현재보다 과거인 경우 예외가 발생한다.")
    void 시작일이_과거인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .startDateTime(LocalDateTime.now().minusDays(2))
                        .build()
        );
    }

    @Test
    @DisplayName("종료일이 시작일보다 과거인 경우 예외가 발생한다.")
    void 종료일이_시작일보다_과거인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .endDateTime(LocalDateTime.now().minusDays(2))
                        .build()
        );
    }

    @Test
    @DisplayName("인원이 2명 이상이 아니면 예외가 발생 한다.")
    void 인원이_2명_이상이_아닌_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .fixedNumberOfPeople(1)
                        .build()
        );
    }

    @Test
    @Disabled
    @DisplayName("위치가 null인 경우 예외가 발생한다.")
    void 위치가_null인_경우() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .location(null)
                        .build()
        );
    }

    @Test
    @DisplayName("위치가 공백이거나 빈 문자열인 경우 예외가 발생한다.")
    void 위치가_공백인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .location(new Location(""))
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명이 null인 경우 예외가 발생한다.")
    void 세부_설명이_null인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details(null)
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명이 공백이거나 빈 문자열인 경우 예외가 발생한다.")
    void 세부_설명이_공백인_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details("")
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명의 글자 수가 300자를 넘으면 예외가 발생합니다.")
    void 글자_수가_300자를_초과한_경우() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details(String.format("%0301d", 301))
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명의 글자 수가 300자인 경우 테스트에 통과합니다.")
    void 글자_수가_300자인_경우() {
        ScheduleFixtureBuilder.builder()
                .details(String.format("%0300d", 300))
                .build();
    }
}