package io.cuki.domain.member.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

}
