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

    @Column(nullable = false)
    private String email;

    public Email(String email) {
        checkValidEmail(email);
        this.email = email.toLowerCase();
    }

    public static void checkValidEmail(String email) {
        if (email == null) {
            log.error("메일 주소는 null 일 수 없습니다. ");
            throw new EmailAddressNotValidException("메일 주소를 입력해주세요.");
        } else if (email.trim().isEmpty()) {
            log.error("메일 주소는 공백 일 수 없습니다. -> {}", email);
            throw new EmailAddressNotValidException("메일 주소를 입력해주세요.");
        } else if (!email.contains("@")) {
            log.error("메일 주소에 @가 없습니다. -> {} ", email);
            throw new EmailAddressNotValidException("메일 주소에 @가 없습니다.");
        }

        final String[] idAndHost = email.split("@");
        final String host = idAndHost[1];

        if (!host.contains(".")) {
            log.error("메일 주소 호스트부에 '.'이 없습니다. -> {}", email);
            throw new EmailAddressNotValidException("이메일 형식이 아닙니다.");
        }
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

