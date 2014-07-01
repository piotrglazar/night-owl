package com.piotrglazar.nightowl.logic;

import org.springframework.stereotype.Component;

@Component
public class StarPositionCalculator {

    public double calculateBoundaryDeclination(double latitude) {
        if (isLatitudeCurrentlyUnsupported(latitude)) {
            throw new IllegalArgumentException(String.format("Latitude %.2f is currently unsupported", latitude));
        }

        return poleCompletion(latitude);
    }

    private double poleCompletion(final double latitude) {
        if (latitude > 0) {
            return 90.0 - latitude;
        } else {
            return -90.0 - latitude;
        }
    }

    private boolean isLatitudeCurrentlyUnsupported(final double latitude) {
        return latitude == 0 || latitude == -90 || latitude == 90;
    }
}
