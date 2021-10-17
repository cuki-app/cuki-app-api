package io.cuki.domain.schedule.exception;

public class ScheduleNotFoundException extends RuntimeException{

    public ScheduleNotFoundException() {
        super("존재하지 않는 모집 일정글입니다.");
    }
}
