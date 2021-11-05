package io.cuki.domain.member.exception;

public class NoSuchRefeshTokenException extends RuntimeException {
    public NoSuchRefeshTokenException(String message) {
        super(message);
    }
}
