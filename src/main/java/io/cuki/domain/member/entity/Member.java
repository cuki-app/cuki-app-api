package io.cuki.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Size(min = 2, max = 8)
    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    public static String CreateRandomNickname() {
        List<String> firstPart = Arrays.asList(
                "두려운", "나쁜", "날으는", "수줍은", "열받은", "화가많은", "행복한", "즐거운", "새침한", "귀여운",
                "그리운", "서운한", "당당한", "시끄러운", "잘생긴", "예쁜", "기분좋은", "기분나쁜", "상쾌한", "배부른",
                "인기많은", "겸손한", "통통한", "짜릿한", "자유로운", "창피한", "멋있는", "심심한", "신난", "작은",
                "소중한", "댄싱머신", "똑똑한", "싱그러운", "잘나가는", "행운의", "나닮은", "배고픈", "깐깐한", "첫사랑"

        );

        List<String> secondPart = Arrays.asList(
                "코뿔소", "부엉이", "올빼미", "독수리", "바다거북", "곰", "호랑이", "고라니", "너구리", "송아",
                "돼지", "사슴", "양", "다람쥐", "고릴라", "사자", "늑대", "코끼리", "고슴도치", "낙타",
                "호빵맨", "둘리", "강아지", "고양이", "붕어", "여우", "악어", "기린", "수달", "거북이",
                "염소", "판다", "참새", "토끼", "참치", "아기", "얼룩말", "짱구", "개미", "오리",
                "천재", "쿠키", "문어", "오징어", "물고기", "개구리", "귀뚜라미", "메뚜기", "감자", "들소"
        );

        Collections.shuffle(firstPart);
        Collections.shuffle(secondPart);

        return firstPart.get(0) + secondPart.get(0);
    }
}
