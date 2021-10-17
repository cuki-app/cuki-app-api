package io.cuki.domain.schedule.utils;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }

}
