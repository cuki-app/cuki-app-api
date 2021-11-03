package io.cuki.infra.email.exception;

public class SendMailFailedException extends RuntimeException {
    public SendMailFailedException(String message) {
        super(message);
    }
}
