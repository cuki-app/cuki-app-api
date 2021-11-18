package io.cuki.domain.member.entity;

import io.cuki.domain.member.exception.NicknameNotValidException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.util.Objects;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {

    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 10)
    private String nickname;

    public Nickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public static String createRandomNickname() {
        final String[] firstPart = {
                "두려운", "나쁜", "날으는", "수줍은", "열받은", "화가많은", "행복한", "즐거운", "새침한", "귀여운",
                "그리운", "서운한", "당당한", "시끄러운", "잘생긴", "예쁜", "기분좋은", "기분나쁜", "상쾌한", "배부른",
                "인기많은", "겸손한", "통통한", "짜릿한", "자유로운", "창피한", "멋있는", "심심한", "신난", "작은",
                "소중한", "댄싱머신", "똑똑한", "싱그러운", "잘나가는", "행운의", "나닮은", "배고픈", "깐깐한", "첫사랑"};

        final String[] secondPart = {
                "코뿔소", "부엉이", "올빼미", "독수리", "바다거북", "곰", "호랑이", "고라니", "너구리", "송아지",
                "돼지", "사슴", "양", "다람쥐", "고릴라", "사자", "늑대", "코끼리", "고슴도치", "낙타",
                "호빵맨", "둘리", "강아지", "고양이", "붕어", "여우", "악어", "기린", "수달", "거북이",
                "염소", "판다", "참새", "토끼", "참치", "아기", "얼룩말", "짱구", "개미", "오리",
                "천재", "쿠키", "문어", "오징어", "물고기", "개구리", "귀뚜라미", "메뚜기", "감자", "들소"
        };

        int firstRandom = (int) (Math.random() * (firstPart.length-1));
        int secondRandom = (int) (Math.random() * (secondPart.length-1));
        int thirdRandom = (int) (Math.random() * 99);

        return firstPart[firstRandom] + secondPart[secondRandom] + thirdRandom;
    }

    public boolean isValidNickname(String nickname) {
        final String NICKNAME_REGEX = "^[0-9|a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]*$";

        if (nickname == null) {
            log.error("닉네임은 null 일 수 없습니다. -> {}", nickname);
            throw new NicknameNotValidException("닉네임을 입력해주세요.");
        } else if (nickname.trim().isEmpty()) {
            log.error("닉네임은 공백 일 수 없습니다. -> {}", nickname);
            throw new NicknameNotValidException("닉네임을 입력해주세요.");
        } else if (nickname.length() < 2 || nickname.length() > 10) {
            log.error("닉네임은 2 ~ 10 사이어야 합니다. -> {}", nickname);
            throw new NicknameNotValidException("닉네임은 2 ~ 10 사이어야 합니다.");
        } else if (!nickname.matches(NICKNAME_REGEX)) {
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
