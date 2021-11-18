package io.cuki.domain.member.entity;

import io.cuki.domain.member.exception.EmailAddressNotValidException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    @Column(nullable = false, unique = true)
    private String email;

    public Email(String email) {
        this.email = email;
    }

    public static Boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if (email == null) {
            log.error("메일 주소는 null 일 수 없습니다. -> {}", email);
            throw new EmailAddressNotValidException("메일 주소를 입력해주세요.");
        } else if (email.trim().isEmpty()) {
            log.error("메일 주소는 공백 일 수 없습니다. -> {}", email);
            throw new EmailAddressNotValidException("메일 주소를 입력해주세요.");
        } else if (!email.matches(EMAIL_REGEX)) {
            log.error("메일 주소가 이메일 형식이 아닙니다. -> {}", email);
            throw new EmailAddressNotValidException("이메일 형식이 아닙니다.");
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}

