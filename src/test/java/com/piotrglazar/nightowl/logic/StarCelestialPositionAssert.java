package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.within;

public class StarCelestialPositionAssert {

    private final StarCelestialPosition starCelestialPosition;

    private StarCelestialPositionAssert(final StarCelestialPosition starCelestialPosition) {
        this.starCelestialPosition = starCelestialPosition;
    }

    public static StarCelestialPositionAssert assertThat(StarCelestialPosition starCelestialPosition) {
        return new StarCelestialPositionAssert(starCelestialPosition);
    }

    public StarCelestialPositionAssert hasZenithDistance(double zenithDistance) {
        Assertions.assertThat(starCelestialPosition.getZenithDistance()).isEqualTo(zenithDistance, within(0.001));
        return this;
    }

    public StarCelestialPositionAssert hasAzimuth(double azimuth) {
        Assertions.assertThat(starCelestialPosition.getAzimuth()).isEqualTo(azimuth, within(0.001));
        return this;
    }
}
