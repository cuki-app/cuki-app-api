package io.cuki.domain.participation.exception;

public class DuplicateParticipationException extends RuntimeException {
    public DuplicateParticipationException(String message) {
        super(message);
    }
}
