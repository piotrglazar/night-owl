package com.piotrglazar.nightowl.coordinates;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Locale;

/**
 * Latitude, in degrees
 */
public class Latitude implements Serializable {

    private static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f %s", Math.abs(latitude), northOrSouth()).trim();
    }

    private String northOrSouth() {
        if (latitude < 0) {
            return "S";
        } else if (latitude == 0) {
            return "";
        } else {
            return "N";
        }
    }
}
