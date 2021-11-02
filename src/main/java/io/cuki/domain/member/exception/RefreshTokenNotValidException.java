package io.cuki.domain.member.exception;

public class RefreshTokenNotValidException extends RuntimeException {
    public RefreshTokenNotValidException(String message) {
        super(message);
    }
}
