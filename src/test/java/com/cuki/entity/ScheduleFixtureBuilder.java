package com.cuki.entity;

import com.cuki.schedule.domain.DateTime;
import com.cuki.schedule.domain.Location;
import com.cuki.schedule.domain.Schedule;

import java.time.LocalDateTime;

public class ScheduleFixtureBuilder {

    private String title = "광화문 교보문고 같이 가실 분 계신가요?";
    private LocalDateTime startDateTime = LocalDateTime.now();
    private LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
    private int fixedNumberOfPeople = 2;
    private int currentNumberOfPeople = 1;
    private Location location = new Location("광화문 교보문고");
    private String description = "장소에 대한 세부 설명입니다.";
    private Member member;

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

    public ScheduleFixtureBuilder fixedNumberOfPeople(int fixedNumberOfPeople) {
        this.fixedNumberOfPeople = fixedNumberOfPeople;
        return this;
    }

    public ScheduleFixtureBuilder currentNumberOfPeople(int currentNumberOfPeople) {
        this.currentNumberOfPeople = currentNumberOfPeople;
        return this;
    }

    public ScheduleFixtureBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public ScheduleFixtureBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ScheduleFixtureBuilder member(Member member) {
        this.member = member;
        return this;
    }

    public Schedule build() {
        return new Schedule(title,
                member,
                new DateTime(startDateTime, endDateTime),
                fixedNumberOfPeople,
                currentNumberOfPeople,
                location,
                description
        );
    }


}
