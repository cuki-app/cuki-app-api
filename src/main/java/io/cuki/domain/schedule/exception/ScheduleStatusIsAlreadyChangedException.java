package io.cuki.domain.schedule.exception;

public class ScheduleStatusIsAlreadyChangedException extends Exception {

    public ScheduleStatusIsAlreadyChangedException(String message) {
        super(message);
    }
}
