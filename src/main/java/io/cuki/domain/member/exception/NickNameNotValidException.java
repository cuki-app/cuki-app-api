package io.cuki.domain.member.exception;

public class NickNameNotValidException extends RuntimeException {
    public NickNameNotValidException(String message) {
        super(message);
    }
}
