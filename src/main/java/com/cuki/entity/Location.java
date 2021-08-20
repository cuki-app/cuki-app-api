package com.cuki.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String place;

    private String roadNameAddress;

    private String numberAddress;

    @NotNull
    private int latitudeX;

    @NotNull
    private int longitudeY;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
