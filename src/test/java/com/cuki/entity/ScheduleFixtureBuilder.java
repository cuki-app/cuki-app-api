package com.cuki.entity;

import java.time.LocalDateTime;

public class ScheduleFixtureBuilder {
    private String title = "title";
    private LocalDateTime due = LocalDateTime.now();
    private int countOfMembers = 3;
    private Location location = new Location("where to go");
    private String detail = "detail";

    public static ScheduleFixtureBuilder builder() {
        return new ScheduleFixtureBuilder();
    }

    public ScheduleFixtureBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ScheduleFixtureBuilder due(LocalDateTime due) {
        this.due = due;
        return this;
    }

    public ScheduleFixtureBuilder countOfMembers(int countOfMembers) {
        this.countOfMembers = countOfMembers;
        return this;
    }

    public ScheduleFixtureBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public ScheduleFixtureBuilder detail(String detail) {
        this.detail = detail;
        return this;
    }

    public Schedule build() {
        return new Schedule(
                title,
                due,
                countOfMembers,
                location,
                detail
        );
    }
}
