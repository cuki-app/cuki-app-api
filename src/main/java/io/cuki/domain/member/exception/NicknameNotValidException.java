package io.cuki.domain.member.exception;

public class NicknameNotValidException extends RuntimeException {
    public NicknameNotValidException(String message) {
        super(message);
    }
}
