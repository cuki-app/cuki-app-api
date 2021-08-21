package com.cuki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "place")
    private String place;

    @Column(name = "road_name_address")
    private String roadNameAddress;

    @Column(name = "number_address")
    private String numberAddress;

    @Column(name = "latitude_x")
    private int latitudeX;

    @Column(name = "longitude_y")
    private int longitudeY;

    @ManyToOne
    private Schedule schedule;
}
