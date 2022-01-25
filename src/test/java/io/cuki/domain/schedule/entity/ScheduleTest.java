package io.cuki.domain.schedule.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.schedule.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("스케쥴 도메인 테스트")
class ScheduleTest {


    @Test
    @DisplayName("제목이 null 인 경우 InvalidTitleException 예외가 발생한다.")
    void testNullTitle() {
        assertThrows(
                InvalidTitleException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title(null)
                        .build()
        );
    }

    @Test
    @DisplayName("제목이 공백으로 되어 있을 경우 InvalidTitleException 예외가 발생한다.")
    void testWhiteSpaceTitle() {
        assertThrows(
                InvalidTitleException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title("     ")
                        .build()
        );
    }

    @Test
    @DisplayName("제목이 비어있을 경우 InvalidTitleException 예외가 발생한다.")
    void testEmptyTitle() {
        assertThrows(
                InvalidTitleException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .title("")
                        .build()
        );
    }

    @Test
    @DisplayName("모집 인원이 1명 미만일 경우 InvalidFixedNumberException 예외가 발생한다.")
    void testFixedNumberIsZero() {
        assertThrows(
                InvalidFixedNumberException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .fixedNumberOfPeople(0)
                        .build()

        );
    }

    @Test
    @DisplayName("세부 설명이 null 인 경우 InvalidDetailsException 예외가 발생한다.")
    void testNullDetails() {
        assertThrows(
                InvalidDetailsException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details(null)
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명이 공백으로 되어있을 경우 InvalidDetailsException 예외가 발생한다.")
    void testWhiteSpaceDetails() {
        assertThrows(
                InvalidDetailsException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details("     ")
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명이 비어있을 경우 InvalidDetailsException 예외가 발생한다.")
    void testEmptyDetails() {
        assertThrows(
                InvalidDetailsException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details("")
                        .build()
        );
    }

    @Test
    @DisplayName("세부 설명이 300자를 초과할 경우 InvalidDetailsException 예외가 발생한다.")
    void testOver300LettersDetails() {
        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras malesuada hendrerit urna, nec rutrum nunc euismod vel. Phasellus eget posuere augue. Cras tincidunt non justo sit amet porta. Aenean venenatis erat velit. Quisque sollicitudin dui eu vulputate euismod. Nam porta blandit tortor sed gravida. Pellentesque vestibulum quis mauris eget vehicula. Quisque at nunc hendrerit, viverra massa sit amet, vestibulum arcu. Sed dignissim eu nunc non pretium. Aenean convallis leo vel ultricies condimentum.";
        assertThrows(
                InvalidDetailsException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .details(lorem)
                        .build()
        );
    }

    @Test
    @DisplayName("시작일이 null 이면 InvalidPeriodException 예외가 발생한다.")
    void testNullStartDateTime() {
        assertThrows(InvalidPeriodException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .startDateTime(null)
                        .build()
        );
    }

    @Test
    @DisplayName("종료일이 null 이면 InvalidPeriodException 예외가 발생한다.")
    void testNullEndDateTime() {
        assertThrows(
                InvalidPeriodException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .endDateTime(null)
                        .build()
        );
    }

    @Test
    @DisplayName("시작일이 현재보다 이전일 경우 InvalidPeriodException 예외가 발생한다.")
    void testStartDateTimeIsBeforeNow() {
        assertThrows(
                InvalidPeriodException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .startDateTime(LocalDateTime.now().minusDays(1))
                        .build()
        );
    }

    @Test
    @DisplayName("종료일이 시작일보다 이전일 경우 InvalidPeriodException 예외가 발생한다.")
    void testEndDateTimeIsBeforeStartDateTime() {
        assertThrows(
                InvalidPeriodException.class,
                () -> {
                    ScheduleFixtureBuilder.builder()
                            .startDateTime(LocalDateTime.now().plusDays(1))
                            .endDateTime(LocalDateTime.now())
                            .build();
                }

        );
    }

    @Test
    @DisplayName("장소가 null 인 경우 InvalidLocationException 예외가 발생한다.")
    void testNullLocation() {
        assertThrows(
                InvalidLocationException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .place(null)
                        .build()
        );
    }

    @Test
    @DisplayName("장소가 공백으로 되어있을 경우 InvalidLocationException 예외가 발생한다.")
    void testWhiteSpaceLocation() {
        assertThrows(
                InvalidLocationException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .place("      ")
                        .build()
        );
    }

    @Test
    @DisplayName("장소가 빈문자열인 경우 InvalidLocationException 예외가 발생한다.")
    void testEmptyLocation() {
        assertThrows(
                InvalidLocationException.class,
                () -> ScheduleFixtureBuilder.builder()
                        .place("")
                        .build()
        );
    }

    @Test
    @DisplayName("스케쥴 등록이 성공하는 테스트")
    void testSuccessScheduleRegistration() {
        final Schedule schedule = ScheduleFixtureBuilder.builder().build();
        assertNotNull(schedule);

        final Member newMember = MemberFixtureBuilder.builder()
                .email("mintcuki@cuki.com")
                .password("mint_cuki_engineer")
                .nickname("민트쿠키")
                .build();
        final Schedule newSchedule = ScheduleFixtureBuilder.builder().member(newMember).build();
        assertNotEquals(schedule, newSchedule);
    }
}