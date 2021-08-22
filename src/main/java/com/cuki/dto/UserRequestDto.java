package com.cuki.dto;

import com.cuki.entity.Authority;
import com.cuki.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 2, max = 8)
    private String nickname;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .activated(true)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
