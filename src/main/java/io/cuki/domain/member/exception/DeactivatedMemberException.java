package io.cuki.domain.member.exception;

public class DeactivatedMemberException extends RuntimeException {
    public DeactivatedMemberException() {
        super("비활성화된 사용자입니다.");
    }
}
