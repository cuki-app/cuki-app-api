package io.cuki.domain.member.exception;

public final class MemberAlreadyExistException extends IllegalStateException{
    public MemberAlreadyExistException(String message) {
        super(message);
    }
}
