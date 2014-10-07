package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Component
public class StarPositionCalculator {

    public double calculateBoundaryDeclination(double latitude) {
        if (isLatitudeCurrentlyUnsupported(latitude)) {
            throw new IllegalArgumentException(String.format("Latitude %.2f is currently unsupported", latitude));
        }

        return poleCompletion(latitude);
    }

    public double poleCompletion(final double latitude) {
        if (latitude > 0) {
            return 90.0 - latitude;
        } else {
            return -90.0 - latitude;
        }
    }

    private boolean isLatitudeCurrentlyUnsupported(final double latitude) {
        return latitude == 0 || latitude == -90 || latitude == 90;
    }

    public StarCelestialPosition calculateCelestialPosition(final Latitude latitude, final LocalTime siderealHourAngle,
                                                            final StarInfo starInfo) {
        final double latitudeRadian = Math.toRadians(latitude.getLatitude());
        final double declinationRadian = Math.toRadians(starInfo.getDeclination());
        final double siderealHourAngleRadian = hourAngleAsArc(siderealHourAngle);
        final double rightAscensionRadian = hourAngleAsArc(starInfo.getRightAscension());
        final double t = siderealHourAngleRadian - rightAscensionRadian;

        final double zenithDistanceRadian = calculateZenithDistance(latitudeRadian, declinationRadian, t);
        final double azimuthRadian = calculateAzimuth(zenithDistanceRadian, declinationRadian, t, latitudeRadian);

        return new StarCelestialPosition(Math.toDegrees(zenithDistanceRadian), Math.toDegrees(azimuthRadian));
    }

    protected double calculateAzimuth(final double zenithDistance, final double declination, final double t, final double latitude) {
        final double cosAzimuth = (sin(declination) * cos(latitude) - cos(declination) * sin(latitude) * cos(t)) / sin(zenithDistance);
        final double azimuth = Math.acos(cosAzimuth);

        if (t < 0 && t > -Math.PI) {
            return azimuth;
        } else if (t > Math.PI) {
            return azimuth;
        } else {
            return 2 * Math.PI - azimuth;
        }
    }

    protected double calculateZenithDistance(final double latitude, final double declination, final double t) {
        final double cosZenith = sin(latitude) * sin(declination) + cos(latitude) * cos(declination) * cos(t);
        return Math.acos(cosZenith);
    }

    public double hourAngleAsArc(final LocalTime hourAngle) {
        final double degrees = hourAngle.toSecondOfDay() / 86400.0 * 360.0;
        return Math.toRadians(degrees);
    }

    public double getMaximumZenithDistance() {
        return 90.0;
    }
}
