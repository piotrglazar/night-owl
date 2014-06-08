package com.piotrglazar.nightowl.coordinates;

import com.google.common.base.Preconditions;

/**
 * Latitude, in degrees
 */
public class Latitude {

    public static final double NORTH = 90.0;

    public static final double SOUTH = -90.0;

    private final double latitude;

    public Latitude(final double latitude) {
        Preconditions.checkArgument(isValid(latitude), "latitude must be between -90 and +90, got %s", latitude);
        this.latitude = latitude;
    }

    public boolean isValid(final double latitude) {
        return latitude <= NORTH && latitude >= SOUTH;
    }

    public double getLatitude() {
        return latitude;
    }
}
