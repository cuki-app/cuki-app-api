package com.cuki.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
public class RefreshToken {

    @Id
    private Long key;
    private String value;

    public RefreshToken updateValue(String refreshToken) {
        this.value = refreshToken;
        return this;
    }
}
