package com.cuki.domain.schedule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Objects;


@Getter
@NoArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place")
    private String place;



    public Location(String place) {
        checkLocationValidation(place);
        this.place = place;
    }

    private void checkLocationValidation(String place) {
        if (Objects.isNull(place)) {
            throw new IllegalArgumentException("장소는 null 일 수 없습니다.");
        }

        if (place.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("장소는 공백으로 둘 수 없습니다.");
        }
    }
}
