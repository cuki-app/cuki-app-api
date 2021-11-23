package io.cuki.domain.member.exception;

public class NicknameAlreadyExistException extends RuntimeException {
    public NicknameAlreadyExistException() {
        super("해당 닉네임은 이미 사용중입니다.");
    }

    public NicknameAlreadyExistException(String message) {
        super(message);
    }
}
