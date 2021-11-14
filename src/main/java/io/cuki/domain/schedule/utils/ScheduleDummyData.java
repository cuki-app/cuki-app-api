package io.cuki.domain.schedule.utils;

import io.cuki.domain.comment.entity.Comment;
import io.cuki.domain.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

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


        Schedule[] schedules = new Schedule[30];
        final LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        for (int i = 0; i < 30; i++) {
            schedules[i] = Schedule.builder()
                .title("북카페 모임 " + (i+1))
                .member(member)
                .dateTime(new SchedulePeriod(startDate, startDate.plusDays(2)))
                .fixedNumberOfPeople(3)
                .currentNumberOfPeople(1)
                .location(new Location("합정 교보문고"))
                .details("더미 데이터 " + (i+1))
                .status(ScheduleStatus.IN_PROGRESS)
                .build();
            schedulesRepository.save(schedules[i]);
        }

        for (int i = 0; i < 30; i++) {
            System.out.println("schedules[i] = " + schedules[i]);
        }

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 100; j++) {
                Comment comment = Comment.builder()
                        .content("댓글이당당당 " + (j+1))
                        .member(member)
                        .schedule(schedules[i])
                        .build();
                System.out.println("comment = " + comment.getContent());
                commentRepository.save(comment);
            }
        }
    }
}
