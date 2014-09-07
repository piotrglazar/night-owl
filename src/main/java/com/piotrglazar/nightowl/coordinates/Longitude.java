package com.piotrglazar.nightowl.coordinates;

import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * Longitude, in degrees
 */
@Immutable
public final class Longitude implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final double EASTWARD = 180.0;
    private static final double WESTWARD = -180.0;

    private final double longitude;

    public Longitude(final double longitude) {
        Preconditions.checkArgument(isValid(longitude), "longitude must be between -180 and 180, got %s", longitude);
        this.longitude = longitude;
    }

    public static boolean isValid(final double longitude) {
        return longitude >= WESTWARD && longitude <= EASTWARD;
    }

    public double getLongitude() {
        return longitude;
    }

    public int toSeconds() {
        // 12h (43200 seconds) is 180 degrees
        // negative value westwards
        // the book says that negative values should go eastwards, so there should be '-longitude'
        // but http://www.jgiesen.de/astro/astroJS/siderealClock/ says otherwise and it seems to be correct
        return (int) (12 * 60 * 60 * longitude / 180.0);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f %s", Math.abs(longitude), eastOrWest()).trim();
    }

    private String eastOrWest() {
        if (longitude < 0) {
            return "W";
        } else if (longitude == 0) {
            return "";
        } else {
            return "E";
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Longitude other = (Longitude) obj;
        return Objects.equals(this.longitude, other.longitude);
    }
}
