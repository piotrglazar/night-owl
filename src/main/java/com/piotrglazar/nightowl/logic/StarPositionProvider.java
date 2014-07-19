package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.StarInfo;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StarPositionProvider {

    private final StarInfoProvider starInfoProvider;
    private final StarPositionCalculator starPositionCalculator;
    private final SiderealHourAngleCalculator siderealHourAngleCalculator;

    @Autowired
    public StarPositionProvider(StarInfoProvider starInfoProvider, StarPositionCalculator starPositionCalculator,
                                SiderealHourAngleCalculator siderealHourAngleCalculator) {
        this.starInfoProvider = starInfoProvider;
        this.starPositionCalculator = starPositionCalculator;
        this.siderealHourAngleCalculator = siderealHourAngleCalculator;
    }

    public long getNumberOfStarsAlwaysVisible(final UserLocation userLocation) {
        return getStarsAlwaysVisible(userLocation).size();
    }

    public List<StarInfo> getStarsAlwaysVisible(final UserLocation userLocation) {
        final Latitude latitude = userLocation.getLatitude();

        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude.getLatitude());

        return getStarsFromDeclinationToPole(declination);
    }

    private List<StarInfo> getStarsFromDeclinationToPole(final double declination) {
        if (declination > 0) {
            return starInfoProvider.getStarsWithDeclinationGreaterThan(declination);
        } else {
            return starInfoProvider.getStarsWithDeclinationLessThan(declination);
        }
    }

    public long getNumberOfStarsNeverVisible(final UserLocation userLocation) {
        return getStarsNeverVisible(userLocation).size();
    }

    public List<StarInfo> getStarsNeverVisible(final UserLocation userLocation) {
        final Latitude latitude = userLocation.getLatitude();

        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude.getLatitude());

        return getStarsFromDeclinationToPole(-declination);
    }

    public long getNumberOfStarsSometimesVisible(final UserLocation userLocation) {
        return getStarsSometimesVisible(userLocation).size();
    }

    public List<StarInfo> getStarsSometimesVisible(final UserLocation userLocation) {
        final Latitude latitude = userLocation.getLatitude();

        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude.getLatitude());

        final double lowerBoundary = Math.min(declination, -declination);
        final double upperBoundary = Math.max(declination, -declination);

        return getNumberOfStarsBetween(lowerBoundary, upperBoundary);
    }

    private List<StarInfo> getNumberOfStarsBetween(final double lowerBoundary, final double upperBoundary) {
        return starInfoProvider.getStarsWithDeclinationBetween(lowerBoundary, upperBoundary);
    }

    public List<StarPositionDto> getStarsPositions(final UserLocation userLocation, final ZonedDateTime date) {
        final List<StarInfo> stars = getStarsAlwaysVisible(userLocation);
        stars.addAll(getStarsSometimesVisible(userLocation));

        final LocalTime siderealHourAngle = siderealHourAngleCalculator.siderealHourAngle(date, userLocation.getLongitude());
        final double maximumZenithDistance = starPositionCalculator.getMaximumZenithDistance();

        return stars.stream()
            .map(si -> new StarPositionDto(si,
                    starPositionCalculator.calculateCelestialPosition(userLocation.getLatitude(), siderealHourAngle, si)))
            .filter(spd -> spd.getZenithDistance() < maximumZenithDistance)
            .collect(Collectors.toList());
    }
}
