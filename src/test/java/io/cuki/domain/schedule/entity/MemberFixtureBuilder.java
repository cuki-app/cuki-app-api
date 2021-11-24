package io.cuki.domain.schedule.entity;

import io.cuki.domain.member.entity.Authority;
import io.cuki.domain.member.entity.Email;
import io.cuki.domain.member.entity.Member;
import io.cuki.domain.member.entity.Nickname;

public class MemberFixtureBuilder {

    private String email = "cuki@cuki.com";
    private String password = "cuki_server_engineer";
    private String nickname = "근사한쿠키";
    private Boolean activated = true;
    private Authority authority = Authority.ROLE_USER;

    public static MemberFixtureBuilder builder() {
        return new MemberFixtureBuilder();
    }

    public MemberFixtureBuilder email(String email) {
        this.email = email;
        return this;
    }

    public MemberFixtureBuilder password(String password) {
        this.password = password;
        return this;
    }

    public MemberFixtureBuilder nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public MemberFixtureBuilder activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public MemberFixtureBuilder authority(Authority authority) {
        this.authority = authority;
        return this;
    }

    public Member build() {
        return Member.builder()
                .email(new Email(email))
                .password(password)
                .nickname(new Nickname(nickname))
                .activated(activated)
                .authority(authority)
                .build();
    }
}
