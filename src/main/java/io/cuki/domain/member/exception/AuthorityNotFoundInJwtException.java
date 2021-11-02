package io.cuki.domain.member.exception;

public class AuthorityNotFoundInJwtException extends RuntimeException {
    public AuthorityNotFoundInJwtException(String message) {
        super(message);
    }
}
