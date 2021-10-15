package io.cuki.domain.member.dto;

import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Authority;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    private String email;
    private String verificationCode;

    public Member toMember(PasswordEncoder passwordEncoder, String nickname) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode("1234"))
                .nickname(nickname)
                .activated(true)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
