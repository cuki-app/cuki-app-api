package io.cuki.domain.participation.entity;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.participation.exception.FixedNumberOutOfBoundsException;
import io.cuki.domain.participation.exception.InvalidReasonForParticipationException;
import io.cuki.domain.schedule.entity.MemberFixtureBuilder;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.ScheduleFixtureBuilder;
import org.junit.jupiter.api.Disabled;
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

    @Test
    @DisplayName("참여 컨펌이 없는 경우 대기자 수가 1명 늘어난다.")
    void 참여_컨펌이_없는_경우() {
        // given
        Schedule scheduleA = createSchedule();
        PermissionResult none = PermissionResult.NONE;
        // when
        scheduleA.updateNumberOfPeopleWaiting(none);
        //then
        assertEquals(1, scheduleA.getNumberOfPeopleWaiting());
        scheduleA.updateNumberOfPeopleWaiting(none);
        assertEquals(2, scheduleA.getNumberOfPeopleWaiting());
    }

    @Test
    @DisplayName("참여 요청을 거절했을 경우 대기자 수가 1명 줄어든다.")
    void 요청을_거절한_경우() {
        // given
        Schedule schedule = createSchedule();
        schedule.updateNumberOfPeopleWaiting(PermissionResult.NONE);
        // when
        PermissionResult reject = PermissionResult.REJECT;
        schedule.updateNumberOfPeopleWaiting(reject);
        // then
        assertEquals(0, schedule.getNumberOfPeopleWaiting());
    }

    @Test
    @DisplayName("참여 요청을 승인한 경우 대기자 수가 1명 줄어든다.")
    void 요청을_승인한_경우_대기자수() {
        // given
        Schedule schedule = createSchedule();
        schedule.updateNumberOfPeopleWaiting(PermissionResult.NONE);
        // when
        PermissionResult accept = PermissionResult.ACCEPT;
        schedule.updateNumberOfPeopleWaiting(accept);
        // then
        assertEquals(0, schedule.getNumberOfPeopleWaiting());
    }

    /**
     * todo 참여를 아무도 안했는데, 그 모집글을 승인 혹은 거절하는 로직을 태우면 대기자(0명)에서 --가 되는데 -가 나올 것 같은데 막는 로직 안짜놓음.
     */
    @Test
    @DisplayName("참여 요청을 승인한 경우 참여 확정자 수가 1명 늘어난다.")
    void 요청을_승인한_경우_확정자수() {
        // given
        Schedule schedule = createSchedule();
        schedule.updateNumberOfPeopleWaiting(PermissionResult.NONE);
        // when
        PermissionResult accept = PermissionResult.ACCEPT;
        schedule.updateNumberOfPeopleWaiting(accept);
        // then
        assertEquals(2, schedule.getCurrentNumberOfPeople(), "확정자 수는 모집글 작성자 1인을 포함한다.");
    }

    @Test
    @DisplayName("참여 확정자 수가 모집 정원을 초과하면 FixedNumberOutOfBoundsException 예외가 발생한다.")
    void 참여확정자_수가_정원을_초과하는_경우() {
        // given
        Schedule schedule = createSchedule();
        // when
        for (int i = 0; i < 5; i++) {
            schedule.updateNumberOfPeopleWaiting(PermissionResult.NONE);
            schedule.updateNumberOfPeopleWaiting(PermissionResult.ACCEPT);
        }
        // then
        assertThrows(FixedNumberOutOfBoundsException.class,
                schedule::checkScheduleConditionForParticipation);
    }

    private Schedule createSchedule() {
        return ScheduleFixtureBuilder.builder().build();
    }

}