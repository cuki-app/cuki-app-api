package io.cuki.domain.member.exception;

public class RefeshTokenNotFoundException extends RuntimeException {
    public RefeshTokenNotFoundException(String message) {
        super(message);
    }
}
