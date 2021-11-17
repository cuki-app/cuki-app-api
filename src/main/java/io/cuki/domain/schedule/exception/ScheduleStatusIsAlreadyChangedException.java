package io.cuki.domain.schedule.exception;

public class ScheduleStatusIsAlreadyChangedException extends RuntimeException {

    public ScheduleStatusIsAlreadyChangedException(String message) {
        super(message);
    }
}
