package io.cuki.domain.schedule.entity;

import io.cuki.domain.member.entity.Authority;
import io.cuki.domain.member.entity.Email;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Nickname;
import java.time.LocalDateTime;

public class ScheduleFixtureBuilder {

    private String title = "Clean Code 북 스터디 모집 합니다!";
    private LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
    private LocalDateTime endDateTime = startDateTime.plusDays(1);
    private Integer fixedNumberOfPeople = 4;
    private String place = "책상과소파 서울대입구점 ";
    private String details = "같이 Clean Code 북 스터디 하실 분 모집합니다. 일주일에 한 챕터씩 공부해와서 토론하는 방식으로 진행합니다.";
    private Member member = Member.builder()
                                    .email(new Email("potato@email.com"))
                                    .password("sweeeeeet_potato")
                                    .nickname(new Nickname("납작한감자"))
                                    .activated(true)
                                    .authority(Authority.ROLE_USER)
                                    .build();

    public static ScheduleFixtureBuilder builder() {
        return new ScheduleFixtureBuilder();
    }

    public ScheduleFixtureBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ScheduleFixtureBuilder startDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public ScheduleFixtureBuilder endDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public ScheduleFixtureBuilder fixedNumberOfPeople(Integer fixedNumberOfPeople) {
        this.fixedNumberOfPeople = fixedNumberOfPeople;
        return this;
    }

    public ScheduleFixtureBuilder place(String place) {
        this.place = place;
        return this;
    }

    public ScheduleFixtureBuilder details(String details) {
        this.details = details;
        return this;
    }

    public ScheduleFixtureBuilder member(Member member) {
        this.member = member;
        return this;
    }


    public Schedule build() {
        final SchedulePeriod dateTime = new SchedulePeriod(startDateTime, endDateTime);
        final Location location = new Location(place);
        return Schedule.create(title, member, dateTime, fixedNumberOfPeople, location, details);
    }




}
