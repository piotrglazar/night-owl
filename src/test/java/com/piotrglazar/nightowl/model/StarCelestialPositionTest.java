package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StarCelestialPositionTest {

    @Test
    public void shouldToStringDisplayHumanReadableMessage() {
        // given
        final StarCelestialPosition starCelestialPosition = new StarCelestialPosition(44.4, 12.3);

        // when
        final String toString = starCelestialPosition.toString();

        // then
        assertThat(toString).contains("zenithDistance", "azimuth", Double.toString(44.4), Double.toString(12.3));
    }
}