package io.cuki.domain.member.exception;

public class RefreshTokenNotMatchException extends RuntimeException {
    public RefreshTokenNotMatchException(String message) {
        super(message);
    }
}
