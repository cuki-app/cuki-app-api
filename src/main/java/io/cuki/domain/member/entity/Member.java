package io.cuki.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.cuki.domain.member.dto.UpdateMyPageInfoRequestDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_member_email", columnNames = {"email"}),
                @UniqueConstraint(name = "uq_member_nickname", columnNames = {"nickname"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(nullable = false)
    private Email email;

    @JsonIgnore
    private String password;

    @Embedded
    @Column(nullable = false)
    private Nickname nickname;

    @Column(nullable = false)
    private Boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @Builder
    private Member(Email email, String password, Nickname nickname, Boolean activated, Authority authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.activated = activated;
        this.authority = authority;
    }

    public void updateMemberInfo(UpdateMyPageInfoRequestDto requestDto) {
        this.nickname.updateNickname(requestDto.getNickname());
    }


}
