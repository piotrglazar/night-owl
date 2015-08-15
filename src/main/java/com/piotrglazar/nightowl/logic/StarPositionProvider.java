package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.MoreCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class StarPositionProvider {

    public static final double BRIGHT_STAR_MAGNITUDE = 1.5;
    public static final String USER_LOCATION_KEY = "#userLocation";
    public static final String STAR_MAGNITUDE_KEY = "#magnitude";
    public static final String USER_LOCATION_AND_STAR_MAG_COMPLEX_KEY = "{" + USER_LOCATION_KEY + "," + STAR_MAGNITUDE_KEY + "}";

    private final StarInfoRepository starInfoRepository;
    private final StarPositionCalculator starPositionCalculator;
    private final SiderealHourAngleCalculator siderealHourAngleCalculator;

    @Autowired
    public StarPositionProvider(StarInfoRepository starInfoRepository, StarPositionCalculator starPositionCalculator,
                                SiderealHourAngleCalculator siderealHourAngleCalculator) {
        this.starInfoRepository = starInfoRepository;
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
            return starInfoRepository.findByDeclinationGreaterThan(declination);
        } else {
            return starInfoRepository.findByDeclinationLessThan(declination);
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

    private List<StarInfo> getNumberOfStarsBetween(double lowerBoundary, double upperBoundary) {
        return starInfoRepository.findByDeclinationBetween(lowerBoundary, upperBoundary);
    }

    public List<StarPositionDto> getStarPositions(UserLocation userLocation, ZonedDateTime date) {
        final List<StarInfo> stars = getStarsAlwaysVisible(userLocation);
        stars.addAll(getStarsSometimesVisible(userLocation));

        return getStarPositions(userLocation, date, stars);
    }

    public List<StarPositionDto> getBrightStarPositions(UserLocation userLocation, ZonedDateTime date, Double magnitude) {
        final List<StarInfo> brightStars = starInfoRepository.findByApparentMagnitudeLessThan(magnitude);
        return getStarPositions(userLocation, date, brightStars);
    }

    @Cacheable(value = ApplicationConfiguration.NIGHT_OWL_CACHE, key = USER_LOCATION_AND_STAR_MAG_COMPLEX_KEY)
    public List<StarPositionDto> getBrightStarPositionsCached(UserLocation userLocation, ZonedDateTime date, Double magnitude) {
        return getBrightStarPositions(userLocation, date, magnitude);
    }

    private List<StarPositionDto> getStarPositions(UserLocation userLocation, ZonedDateTime date, List<StarInfo> stars) {
        final LocalTime siderealHourAngle = siderealHourAngleCalculator.siderealHourAngle(date, userLocation.getLongitude());
        final double maximumZenithDistance = starPositionCalculator.getMaximumZenithDistance();

        return stars.stream()
            .map(si -> new StarPositionDto(si,
                    starPositionCalculator.calculateCelestialPosition(userLocation.getLatitude(), siderealHourAngle, si)))
            .filter(spd -> spd.getZenithDistance() < maximumZenithDistance)
            .collect(MoreCollectors.toImmutableList());
    }
}
