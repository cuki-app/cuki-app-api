package com.cuki.dto;

import com.cuki.entity.Authority;
import com.cuki.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private Authority authority;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getAuthority());
    }
}
