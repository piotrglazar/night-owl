package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StarPositionProvider {

    private final StarInfoProvider starInfoProvider;
    private final StarPositionCalculator starPositionCalculator;

    @Autowired
    public StarPositionProvider(final StarInfoProvider starInfoProvider, final StarPositionCalculator starPositionCalculator) {
        this.starInfoProvider = starInfoProvider;
        this.starPositionCalculator = starPositionCalculator;
    }

    public long getNumberOfStarsAlwaysVisible(UserLocation userLocation) {
        final Latitude latitude = userLocation.getLatitude();

        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude.getLatitude());

        return getNumberOfStarsFromDeclinationToPole(declination);
    }

    private long getNumberOfStarsFromDeclinationToPole(final double declination) {
        if (declination > 0) {
            return starInfoProvider.getStarsWithDeclinationGreaterThan(declination).size();
        } else {
            return starInfoProvider.getStarsWithDeclinationLessThan(declination).size();
        }
    }

    public long getNumberOfStarsNeverVisible(final UserLocation userLocation) {
        final Latitude latitude = userLocation.getLatitude();

        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude.getLatitude());

        return getNumberOfStarsFromDeclinationToPole(-declination);
    }

    public long getNumberOfStarsSometimesVisible(final UserLocation userLocation) {
        final Latitude latitude = userLocation.getLatitude();

        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude.getLatitude());

        final double lowerBoundary = Math.min(declination, -declination);
        final double upperBoundary = Math.max(declination, -declination);

        return getNumberOfStarsBetween(lowerBoundary, upperBoundary);
    }

    private long getNumberOfStarsBetween(final double lowerBoundary, final double upperBoundary) {
        return starInfoProvider.getStarsWithDeclinationBetween(lowerBoundary, upperBoundary).size();
    }
}
