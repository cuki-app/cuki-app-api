package com.cuki.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    private String key;
    private String value;

    public RefreshToken updateValue(String refreshToken) {
        this.value = refreshToken;
        return this;
    }
}
