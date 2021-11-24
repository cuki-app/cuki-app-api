package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.exception.InvalidLocationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("스케쥴 - Location 도메인 테스트")
class LocationTest {


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