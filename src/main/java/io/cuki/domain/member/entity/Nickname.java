package io.cuki.domain.member.entity;

import io.cuki.domain.member.exception.NicknameNotValidException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {

    @Column(nullable = false)
    private String nickname;

    private static final String NICKNAME_REGEX = "^[0-9|a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]*$";
    private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);

    private static final String[] CAPITAL = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하"
    };

    public Nickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public static String createRandomNickname() {
        int idx = (int) (Math.random() * CAPITAL.length);
        LocalDateTime now = LocalDateTime.now();

        String FIRST_PART = CAPITAL[idx];
        String SECOND_PART = String.valueOf(now.getYear()).substring(3);
        int THIRD_PART = now.getDayOfYear();
        int FOURTH_PART = (int) (Math.random() * 99999);

        return FIRST_PART + SECOND_PART + THIRD_PART + FOURTH_PART;
    }

    public boolean isValidNickname(String nickname) {
        if (nickname == null) {
            log.error("닉네임은 null 일 수 없습니다.");
            throw new NicknameNotValidException("닉네임을 입력해주세요.");
        } else if (nickname.trim().isEmpty()) {
            log.error("닉네임은 공백 일 수 없습니다. -> {}", nickname);
            throw new NicknameNotValidException("닉네임을 입력해주세요.");
        } else if (nickname.length() < 2 || nickname.length() > 10) {
            log.error("닉네임은 2 ~ 10 사이어야 합니다. -> {}", nickname);
            throw new NicknameNotValidException("닉네임은 2 ~ 10 사이어야 합니다.");
        }

        final Matcher matcher = NICKNAME_PATTERN.matcher(nickname);
        if (!matcher.matches()) {
            log.error("닉네임에 특수 문자가 있습니다. -> {}", nickname);
            throw new NicknameNotValidException("닉네임은 한글, 영문, 숫자만 사용할 수 있습니다.");
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nickname nickname1 = (Nickname) o;
        return Objects.equals(nickname, nickname1.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
