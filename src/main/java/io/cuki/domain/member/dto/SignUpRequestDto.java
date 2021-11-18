package io.cuki.domain.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    private String email;
    private String verificationCode;

}
