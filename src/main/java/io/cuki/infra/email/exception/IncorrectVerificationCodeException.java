package io.cuki.infra.email.exception;

public class IncorrectVerificationCodeException extends RuntimeException {
    public IncorrectVerificationCodeException() {
        super("인증번호가 틀렸습니다.");
    }
}
