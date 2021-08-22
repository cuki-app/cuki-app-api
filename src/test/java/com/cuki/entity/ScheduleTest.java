package com.cuki.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * sut: System Under Test - 테스트 대상
 */
@DisplayName("일정 생성 테스트")
class ScheduleTest {

    @DisplayName("제목이 없는 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testTitleOmitted() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title(null)
                        .build()
        );
    }

    @DisplayName("제목이 공백이나 빈 문자열인 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testTitleIsWhiteSpaceOrEmptyString() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title("")
                        .build()
        );
    }

    @DisplayName("일정이 null이면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testDueIsOmitted() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .due(null)
                        .build()
        );
    }

    @DisplayName("일정이 과거인 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testDueCannotBePast() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .due(LocalDateTime.now().minusDays(5))
                        .build()
        );
    }

    @DisplayName("모집 인원은 한 명 이상이여야 한다.")
    @Test
    void testCountOfMembersShouldBeGreaterThan1() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .countOfMembers(0)
                        .build()
        );
    }

    @DisplayName("위치가 null인 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testLocationCannotBeNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .location(null)
                        .build()
        );
    }

    @DisplayName("모집 내용이 null이라면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testDetailCannotBeNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .detail(null)
                        .build()
        );
    }

    @DisplayName("모집 내용이 빈 문자열이나 공백인 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testDetailCannotBeEmptyOrWhiteSpace() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .detail("")
                        .build()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .detail(" ")
                        .build()
        );
    }

    @DisplayName("모집 내용이 300자를 넘어가면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void testDetailCannotBeGreaterThan300() {
        final String detail =
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "a" ;
        assertThrows(
                IllegalArgumentException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .detail(detail)
                        .build()
        );
    }
}