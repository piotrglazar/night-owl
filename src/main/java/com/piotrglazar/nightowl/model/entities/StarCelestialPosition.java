package com.piotrglazar.nightowl.model.entities;

import com.google.common.base.MoreObjects;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class StarCelestialPosition {

    // in degrees
    private final double zenithDistance;

    // in degrees
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
        return MoreObjects.toStringHelper(this)
                .add("zenithDistance", zenithDistance)
                .add("azimuth", azimuth)
                .toString();
    }
}
