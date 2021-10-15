package io.cuki.domain.schedule.utils;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.schedule.entity.Location;
import io.cuki.domain.schedule.entity.Schedule;
import io.cuki.domain.schedule.entity.SchedulePeriod;
import io.cuki.domain.schedule.entity.ScheduleStatus;
import io.cuki.domain.member.entity.Authority;
import io.cuki.domain.member.repository.MemberRepository;
import io.cuki.domain.schedule.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ScheduleDummyData implements InitializingBean {

    private final MemberRepository memberRepository;
    private final SchedulesRepository schedulesRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        final Member member = Member.builder()
                .id(1L)
                .email("potato@email.com")
                .password("sweeeeeet_potato")
                .nickname("납작한감자")
                .activated(true)
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member);


        final LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        for (int i = 0; i < 30; i++) {
            Schedule schedule = Schedule.builder()
                    .title("북카페 모임 " + (i+1))
                    .member(member)
                    .dateTime(new SchedulePeriod(startDate, startDate.plusDays(2)))
                    .fixedNumberOfPeople(3)
                    .currentNumberOfPeople(1)
                    .location(new Location("합정 교보문고"))
                    .details("더미 데이터 " + (i+1))
                    .status(ScheduleStatus.IN_PROGRESS)
                    .build();
            schedulesRepository.save(schedule);
        }
    }
}
