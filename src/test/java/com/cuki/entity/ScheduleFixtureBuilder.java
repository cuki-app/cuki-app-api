package com.cuki.entity;

import java.time.LocalDateTime;

public class ScheduleFixtureBuilder {

    private String title = "광화문 교보문고 같이 가실 분 계신가요?";
    private LocalDateTime startDateTime = LocalDateTime.now();
    private LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);
    private int participants = 2;
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

    public ScheduleFixtureBuilder participants(int participants) {
        this.participants = participants;
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

//    public Schedule build() {
//        return new Schedule(title,
//                            startDateTime,
//                            endDateTime,
//                            participants,
//                            location,
//                            description
//        );
//    }

    public Schedule build() {
        return new Schedule(title,

                new DateTime(startDateTime, endDateTime),
                participants,
                location,
                description
        );
    }


}
