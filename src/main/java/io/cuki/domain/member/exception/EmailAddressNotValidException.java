package io.cuki.domain.member.exception;

public class EmailAddressNotValidException extends RuntimeException {
    public EmailAddressNotValidException(String message) {
        super(message);
    }
}
