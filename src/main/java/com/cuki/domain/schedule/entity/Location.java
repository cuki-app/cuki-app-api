package com.cuki.domain.schedule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "장소는 null 이면 안됩니다.")
    @NotBlank(message = "장소는 빈문자열 또는 공백이어서는 안됩니다.")
    @Column(name = "place")
    private String place;



    public Location(String place) {
        this.place = place;
    }
}
