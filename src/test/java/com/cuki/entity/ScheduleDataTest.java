package com.cuki.entity;

import com.cuki.domain.member.entity.Member;
import com.cuki.domain.member.repository.MemberRepository;
import com.cuki.domain.schedule.entity.DateTime;
import com.cuki.domain.schedule.entity.Location;
import com.cuki.domain.schedule.entity.Schedule;
import com.cuki.domain.schedule.repository.SchedulesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
public class ScheduleDataTest {

    @Autowired
    SchedulesRepository schedulesRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertDummy() {

        final Member member = memberRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("없는 사용자 입니다."));

        IntStream.range(1, 14).forEach(i -> {
            final Schedule schedule = Schedule.builder()
                    .title("북카페 모임 " + i)
                    .member(member)
                    .dateTime(new DateTime(LocalDateTime.of(2021, 9, 21, 21, 32), LocalDateTime.of(2021, 10, 13, 21, 30)))
                    .fixedNumberOfPeople(3)
                    .currentNumberOfPeople(1)
                    .location(new Location("합정 교보문고"))
                    .details("더미 데이터 " + i)
                    .build();
            schedulesRepository.save(schedule);
        });

    }

    @Test
    public void getPage() {

        Pageable page = PageRequest.of(0, 4);
        // page 0: schedule no.1 ~ no.4
        // page 1: schedule no.5 ~ no.8
        // page 2: schedule no.9 ~ no.12
        // page 3: schedule no.13

        final Slice<Schedule> result = schedulesRepository.findAll(page);

        result.forEach(s -> {
            System.out.println("제목 : " + s.getTitle());
            System.out.println("내용 : " + s.getDetails());
        });
    }
}
