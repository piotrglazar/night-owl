package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.StarInfoDetails;
import org.junit.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class StarPositionDtoTest {
    @Test
    public void shouldToStringDisplayHumanReadableMessage() {
        // given
        final StarPositionDto starPositionDto = new StarPositionDto(starInfo(), starCelestialPosition());

        // when
        final String toString = starPositionDto.toString();

        // then
        assertThat(toString).contains("starInfo", "starCelestialPosition");
    }

    private StarCelestialPosition starCelestialPosition() {
        return new StarCelestialPosition(22.2, 33.4);
    }

    private StarInfo starInfo() {
        return new StarInfo(LocalTime.of(12, 35), 44.4, "A0", new StarInfoDetails("Sirius"), 0.0);
    }
}