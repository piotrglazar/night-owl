package com.piotrglazar.nightowl.coordinates;

import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * Latitude, in degrees
 */
@Immutable
public final class Latitude implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final double NORTH = 90.0;
    public static final double SOUTH = -90.0;
    public static final double MAXIMUM_ABS_LATITUDE = NORTH;

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
        if (isSouth()) {
            return "S";
        } else if (isNorth()) {
            return "N";
        } else {
            return "";
        }
    }

    public boolean isNorth() {
        return latitude > 0;
    }

    public boolean isSouth() {
        return latitude < 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Latitude other = (Latitude) obj;
        return Objects.equals(this.latitude, other.latitude);
    }
}
