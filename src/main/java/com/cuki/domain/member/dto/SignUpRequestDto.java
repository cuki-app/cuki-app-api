package com.cuki.domain.member.dto;

import com.cuki.domain.member.domain.Authority;
import com.cuki.domain.member.domain.Member;
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
