package io.cuki.infra.email.exception;

public class SendMailFailedException extends Throwable {
    public SendMailFailedException(String message) {
        super(message);
    }
}
