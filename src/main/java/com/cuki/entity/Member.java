package com.cuki.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Size(min = 2, max = 8)
//    @Column(nullable = false, unique = true)
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean activated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private Set<Participation> participationList = new HashSet<>();

}
