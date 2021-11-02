package io.cuki.domain.member.exception;

public class MemberAlreadyLoggedOutException extends RuntimeException {
    public MemberAlreadyLoggedOutException(String message) {
        super(message);
    }
}
