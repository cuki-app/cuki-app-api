package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.exception.InvalidLocationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("스케쥴 - Location 도메인 테스트")
class LocationTest {

    Logger log = LoggerFactory.getLogger(LocationTest.class);

    @BeforeAll
    static void setup() {
        Logger log = LoggerFactory.getLogger(LocationTest.class);
        log.info("@BeforeAll - executes once before all test methods in this class");
    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - executes before each test method in this class");
    }

    @Test
    @DisplayName("장소가 null 인 경우 InvalidLocationException 예외가 발생한다.")
    void testNullLocation() {
        assertThrows(
                InvalidLocationException.class,
                () -> new Location(null)
        );
    }

    @Test
    @DisplayName("장소가 공백으로 되어있을 경우 InvalidLocationException 예외가 발생한다.")
    void testWhiteSpaceLocation() {
        assertThrows(
                InvalidLocationException.class,
                () -> new Location("   ")
        );
    }

    @Test
    @DisplayName("장소가 빈문자열인 경우 InvalidLocationException 예외가 발생한다.")
    void testEmptyLocation() {
        assertThrows(
                InvalidLocationException.class,
                () -> new Location("")
        );
    }
}