package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.AbstractContextTest;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.guava.GuavaCacheManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.piotrglazar.nightowl.DatabaseTestConfiguration.STARS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

@SuppressWarnings("unchecked")
public class StarPositionProviderContextTest extends AbstractContextTest {

    @Autowired
    private StarPositionProvider starPositionProvider;

    @Autowired
    private GuavaCacheManager guavaCacheManager;

    @Test
    public void shouldGetStarVisibilityCountsForNorth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);

        // when
        final long numberOfStarsAlwaysVisible = starPositionProvider.getNumberOfStarsAlwaysVisible(userLocation);
        final long numberOfStarsSometimesVisible = starPositionProvider.getNumberOfStarsSometimesVisible(userLocation);
        final long numberOfStarsNeverVisible = starPositionProvider.getNumberOfStarsNeverVisible(userLocation);

        // then
        assertThat(numberOfStarsAlwaysVisible).isEqualTo(3);
        assertThat(numberOfStarsSometimesVisible).isEqualTo(2);
        assertThat(numberOfStarsNeverVisible).isEqualTo(1);
    }

    @Test
    public void shouldGetStarVisibilityCountsForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);

        // when
        final long numberOfStarsAlwaysVisible = starPositionProvider.getNumberOfStarsAlwaysVisible(userLocation);
        final long numberOfStarsSometimesVisible = starPositionProvider.getNumberOfStarsSometimesVisible(userLocation);
        final long numberOfStarsNeverVisible = starPositionProvider.getNumberOfStarsNeverVisible(userLocation);

        // then
        assertThat(numberOfStarsAlwaysVisible).isEqualTo(1);
        assertThat(numberOfStarsSometimesVisible).isEqualTo(2);
        assertThat(numberOfStarsNeverVisible).isEqualTo(3);
    }

    @Test
    public void shouldCalculateStarPositions() {
        // given
        final UserLocation userLocation = userLocationWithLatitudeAndLongitude(52.0, 21.0);
        final ZonedDateTime date = ZonedDateTime.of(LocalDate.of(2014, 7, 16), LocalTime.of(23, 52), ZoneId.of("Europe/Paris"));

        // when
        final List<StarPositionDto> starsPositions = starPositionProvider.getStarPositions(userLocation, date);

        // then
        assertThat(starsPositions).hasSize(5);
        containsWithTolerance(starsPositions, STARS.get(0), 35.467, 20.616);
        containsWithTolerance(starsPositions, STARS.get(1), 41.277, 26.029);
        containsWithTolerance(starsPositions, STARS.get(2), 71.350, 19.922);
        containsWithTolerance(starsPositions, STARS.get(3), 82.621, 297.299);
        containsWithTolerance(starsPositions, STARS.get(4), 89.905, 229.179);
    }

    @Test
    public void shouldCalculatePositionsForBrightStars() {
        // given
        final UserLocation userLocation = userLocationWithLatitudeAndLongitude(52.0, 21.0);
        final ZonedDateTime date = ZonedDateTime.of(LocalDate.of(2014, 7, 16), LocalTime.of(23, 52), ZoneId.of("Europe/Paris"));

        // when
        final List<StarPositionDto> starsPositions = starPositionProvider.getBrightStarPositions(userLocation, date);

        // then
        assertThat(starsPositions).hasSize(4);
    }

    @Test
    public void shouldCacheStarPositionCalculations() {
        // given
        final UserLocation userLocation = userLocationWithLatitudeAndLongitude(52.0, 21.0);
        final ZonedDateTime date = ZonedDateTime.of(LocalDate.of(2014, 7, 16), LocalTime.of(23, 52), ZoneId.of("Europe/Paris"));

        // when
        final List<StarPositionDto> brightStarPositions = starPositionProvider.getBrightStarPositionsCached(userLocation, date);

        // then
        final List<StarPositionDto> brightStarPositionsCached = getCachedStarPositions(userLocation);
        assertThat(brightStarPositions).isEqualTo(brightStarPositionsCached);
    }

    private List<StarPositionDto> getCachedStarPositions(final UserLocation userLocation) {
        return (List<StarPositionDto>) guavaCacheManager.getCache("nightOwlCache").get(userLocation).get();
    }

    private void containsWithTolerance(List<StarPositionDto> starsPositions, StarInfo starInfo, double zenithDistance, double azimuth) {
        final StarPositionDto starPositionDto = starsPositions.stream()
                .filter(si -> si.getStarInfo().getSpectralType().equals(starInfo.getSpectralType()))
                .findFirst()
                .get();
        assertThat(starPositionDto.getStarCelestialPosition().getAzimuth()).isEqualTo(azimuth, offset(0.001));
        assertThat(starPositionDto.getStarCelestialPosition().getZenithDistance()).isEqualTo(zenithDistance, offset(0.001));
    }

    private UserLocation userLocationWithLatitude(final double latitude) {
        return new UserLocation(new Latitude(latitude), null, null);
    }

    private UserLocation userLocationWithLatitudeAndLongitude(final double latitude, final double longitude) {
        return new UserLocation(new Latitude(latitude), new Longitude(longitude), null);
    }
}
