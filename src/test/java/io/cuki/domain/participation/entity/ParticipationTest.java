package io.cuki.domain.participation.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.participation.exception.InvalidReasonForParticipationException;
import io.cuki.domain.schedule.entity.MemberFixtureBuilder;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleFixtureBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Participation 도메인 테스트")
class ParticipationTest {

    @Test
    @DisplayName("참여 이유가 null 인 경우 InvalidReasonForParticipationException 예외가 발생한다.")
    void test_Null_ReasonForParticipation() {
        final Member member = MemberFixtureBuilder.builder().build();
        final Schedule schedule = ScheduleFixtureBuilder.builder().build();
        assertThrows(
                InvalidReasonForParticipationException.class,
                () -> Participation.create(member, schedule, null)
        );
    }

    @Test
    @DisplayName("참여 이유가 공백인 경우 InvalidReasonForParticipationException 예외가 발생한다.")
    void test_WhiteSpace_ReasonForParticipation() {
        final Member member = MemberFixtureBuilder.builder().build();
        final Schedule schedule = ScheduleFixtureBuilder.builder().build();
        assertThrows(
                InvalidReasonForParticipationException.class,
                () -> Participation.create(member, schedule, "       ")
        );
    }

    @Test
    @DisplayName("참여 이유가 비어있는 경우 InvalidReasonForParticipationException 예외가 발생한다.")
    void test_Empty_ReasonForParticipation() {
        final Member member = MemberFixtureBuilder.builder().build();
        final Schedule schedule = ScheduleFixtureBuilder.builder().build();
        assertThrows(
                InvalidReasonForParticipationException.class,
                () -> Participation.create(member, schedule, "")
        );
    }

}