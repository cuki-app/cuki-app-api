package com.cuki.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int numberOfParticipants;

    @NotNull
    private int count;

    // 테스트 -> OneToMany, manyToOne 으로 변경할 것
//    @ManyToMany
//    private List<Member> members;

    @OneToMany(mappedBy = "participation")
    private List<MemberParticipation> memberParticipations = new ArrayList<>();


    public Participation(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }


    public void update(int currentCount, List<MemberParticipation> memberParticipations) {
        this.count = currentCount;
        this.memberParticipations = memberParticipations;
    }

//    public void update(int currentCount, List<Member> members) {
//        this.count = currentCount;
//        this.members = members;
//    }
}
