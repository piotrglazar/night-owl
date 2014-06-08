package com.piotrglazar.nightowl.coordinates;

import com.google.common.base.Preconditions;

/**
 * Longitude, in degrees
 */
public class Longitude {

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
        // negative value eastwards
        return (int) (12 * 60 * 60 * -longitude / 180.0);
        // TODO: minus or plus? I will stick to minus now, the same as in the book
        // http://www.jgiesen.de/astro/astroJS/siderealClock/
        //return (int) (12 * 60 * 60 * longitude / 180.0);
    }
}
