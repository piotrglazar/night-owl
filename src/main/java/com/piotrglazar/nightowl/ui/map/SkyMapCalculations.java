package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.logic.StarPositionCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkyMapCalculations {

    private final StarPositionCalculator starPositionCalculator;

    @Autowired
    public SkyMapCalculations(final StarPositionCalculator starPositionCalculator) {
        this.starPositionCalculator = starPositionCalculator;
    }

    public int distanceFromCenter(final int y, final int radius, final double angle) {
        final double distanceFromPoleInAngles = starPositionCalculator.poleCompletion(angle);
        return y - (int) scaleLength(radius, distanceFromPoleInAngles);
    }

    private long scaleLength(final int radius, final double distanceFromPoleInAngles) {
        return Math.round(distanceFromPoleInAngles * radius / Latitude.MAXIMUM_ABS_LATITUDE);
    }

    public Point starLocation(final int x, final int y, final int radius, final double azimuth, final double zenithDistance) {
        final double azimuthRadian = Math.toRadians(azimuth);
        final double xPoint = Math.sin(azimuthRadian) * scaleLength(radius, zenithDistance) + x;
        final double yPoint = -Math.cos(azimuthRadian) * scaleLength(radius, zenithDistance) + y;

        return new Point((int) xPoint, (int) yPoint);
    }

}
