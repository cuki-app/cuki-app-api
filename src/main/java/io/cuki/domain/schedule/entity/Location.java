package io.cuki.domain.schedule.entity;

import io.cuki.domain.schedule.exception.InvalidLocationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import java.util.Objects;


@Slf4j
@Getter
@NoArgsConstructor
@Embeddable
public class Location {

    private String place;


    public Location(String place) {
        checkLocationValidation(place);
        this.place = place;
    }

    private void checkLocationValidation(String place) {
        if (Objects.isNull(place)) {
            log.error("장소 = {}", place);
            throw new InvalidLocationException("장소는 필수 입력 사항입니다.");
        }

        if (place.replace(" ", "").isEmpty()) {
            log.error("장소는 공백 이거나 빈문자열이어서는 안됩니다. = {}", place);
            throw new InvalidLocationException("장소는 공백으로 두실 수 없습니다.");
        }
    }
}
