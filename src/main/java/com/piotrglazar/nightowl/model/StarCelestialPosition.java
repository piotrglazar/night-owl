package com.piotrglazar.nightowl.model;

import com.google.common.base.Objects;

public class StarCelestialPosition {

    private final double zenithDistance;
    private final double azimuth;

    public StarCelestialPosition(final double zenithDistance, final double azimuth) {
        this.zenithDistance = zenithDistance;
        this.azimuth = azimuth;
    }

    public double getZenithDistance() {
        return zenithDistance;
    }

    public double getAzimuth() {
        return azimuth;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("zenithDistance", zenithDistance)
                .add("azimuth", azimuth)
                .toString();
    }
}
