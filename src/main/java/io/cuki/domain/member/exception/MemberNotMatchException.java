package io.cuki.domain.member.exception;

public class MemberNotMatchException extends RuntimeException {

    public MemberNotMatchException(String message) {
        super(message);
    }
}
