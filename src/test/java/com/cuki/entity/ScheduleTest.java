package com.cuki.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

@DisplayName("일정 등록 테스트")
class ScheduleTest {

    /**
     * action: 필수 정보는 모두 존재해야 한다 (제목, 날짜, 인원, 위치, 세부설명)
     * data : 제목, 날짜, 인원, 위치, 세부설명
     */

    @Test
    @DisplayName("제목이 null인 경우 예외가 발생한다.")
    void 제목이_없는_경우() {
        String title = null;
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
        int participants = 2;
        Location place = new Location("광화문 교보문고");
        String description = "장소에 대한 세부 설명입니다.";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Schedule(title, startDateTime, endDateTime, participants, place, description)
        );
    }

    @Test
    @DisplayName("제목이 공백인 경우 예외가 발생한다.")
    void 제목이_공백인_경우() {
        String title = "";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
        int participants = 2;
        Location place = new Location("광화문 교보문고");
        String description = "장소에 대한 세부 설명입니다.";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Schedule(title, startDateTime, endDateTime, participants, place, description)
        );
    }

    @Test
    @DisplayName("시작일이 null인 경우 예외가 발생한다.")
    void 시작일이_null인_경우() {
        String title = "책 구경하러 갈 사람";
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
        int participants = 2;
        Location place = new Location("광화문 교보문고");
        String description = "장소에 대한 세부 설명입니다.";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Schedule(title, startDateTime, endDateTime, participants, place, description)
        );
    }

    @Test
    @DisplayName("종료일이 null인 경우 예외가 발생한다.")
    void 종료일이_null인_경우() {
        String title = "책 구경하러 갈 사람";
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = null;
        int participants = 2;
        Location place = new Location("광화문 교보문고");
        String description = "장소에 대한 세부 설명입니다.";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Schedule(title, startDateTime, endDateTime, participants, place, description)
        );
    }

    /**
     * 1. 시작일이 현재보다 과거인 경우
     * 2. 시작일이 종료일보다 미래인 경우     08-25 ~ 08-24
     */
}