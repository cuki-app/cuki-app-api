package com.cuki.entity;

import java.time.LocalDateTime;

public class Schedule {

    public Schedule(String title, LocalDateTime due, int countOfMembers, Location location, String detail) {
        checkTitleValidation(title);
        checkDueValidation(due);
        checkMemberCountValidation(countOfMembers);
        checkLocationValidation(location);
        checkDetailValidation(detail);
    }

    private void checkDetailValidation(String detail) {
        if (detail == null) {
            throw new IllegalArgumentException("Detail cannot be null!");
        }

        if (detail.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("Detail cannot be empty or whitespace.");
        }

        if (detail.length() > 300) {
            throw new IllegalArgumentException("Length of detail cannot be greater than 300");
        }
    }

    private void checkLocationValidation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
    }

    private void checkMemberCountValidation(int countOfMembers) {
        if (countOfMembers < 1) {
            throw new IllegalArgumentException("");
        }
    }

    private void checkDueValidation(LocalDateTime due) {
        if (due == null) {
            throw new IllegalArgumentException("Due cannot be null!");
        }

        if (LocalDateTime.now().isAfter(due)) {
            throw new IllegalArgumentException("Due cannot be past.");
        }
    }

    private void checkTitleValidation(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null!");
        }

        if (title.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty or whitespace.");
        }
    }
}
