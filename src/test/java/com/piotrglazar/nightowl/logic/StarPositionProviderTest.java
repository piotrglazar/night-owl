package com.piotrglazar.nightowl.logic;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StarPositionProviderTest {

    @Mock
    private SiderealHourAngleCalculator siderealHourAngleCalculator;

    @Mock
    private StarInfoRepository starInfoRepository;

    @Mock
    private StarPositionCalculator starPositionCalculator;

    @InjectMocks
    private StarPositionProvider provider;

    @Test
    public void shouldGetAlwaysVisibleStarsCountForNorth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final double declination = 40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(52.0)).willReturn(declination);
        given(starInfoRepository.findByDeclinationGreaterThan(declination)).willReturn(starInfo);

        // when
        final long numberOfStarsAlwaysVisible = provider.getNumberOfStarsAlwaysVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(52.0);
        verify(starInfoRepository).findByDeclinationGreaterThan(declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoRepository);
        assertThat(numberOfStarsAlwaysVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetSometimesVisibleStarsCountForNorth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final double declination = 40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(52.0)).willReturn(declination);
        given(starInfoRepository.findByDeclinationBetween(-declination, declination)).willReturn(starInfo);

        // when
        final long numberOfStarsSometimesVisible = provider.getNumberOfStarsSometimesVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(52.0);
        verify(starInfoRepository).findByDeclinationBetween(-declination, declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoRepository);
        assertThat(numberOfStarsSometimesVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetNeverVisibleStarsCountForNorth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final double declination = 40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(52.0)).willReturn(declination);
        given(starInfoRepository.findByDeclinationLessThan(-declination)).willReturn(starInfo);

        // when
        final long numberOfStarsNeverVisible = provider.getNumberOfStarsNeverVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(52.0);
        verify(starInfoRepository).findByDeclinationLessThan(-declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoRepository);
        assertThat(numberOfStarsNeverVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetAlwaysVisibleStarsCountForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);
        final double declination = -40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(-52.0)).willReturn(declination);
        given(starInfoRepository.findByDeclinationLessThan(declination)).willReturn(starInfo);

        // when
        final long numberOfStarsAlwaysVisible = provider.getNumberOfStarsAlwaysVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(-52.0);
        verify(starInfoRepository).findByDeclinationLessThan(declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoRepository);
        assertThat(numberOfStarsAlwaysVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetSometimesVisibleStarsCountForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);
        final double declination = -40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(-52.0)).willReturn(declination);
        given(starInfoRepository.findByDeclinationBetween(declination, -declination)).willReturn(starInfo);

        // when
        final long numberOfStarsSometimesVisible = provider.getNumberOfStarsSometimesVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(-52.0);
        verify(starInfoRepository).findByDeclinationBetween(declination, -declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoRepository);
        assertThat(numberOfStarsSometimesVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetNumberOfStarsNeverVisibleForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);
        final double declination = -40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(-52.0)).willReturn(declination);
        given(starInfoRepository.findByDeclinationGreaterThan(-declination)).willReturn(starInfo);

        // when
        final long numberOfStarsNeverVisible = provider.getNumberOfStarsNeverVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(-52.0);
        verify(starInfoRepository).findByDeclinationGreaterThan(-declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoRepository);
        assertThat(numberOfStarsNeverVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetStarPositionsOnTheSky() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final ZonedDateTime arbitraryDate = ZonedDateTime.of(2014, 7, 16, 23, 29, 0, 0, ZoneId.of("UTC"));
        final List<StarInfo> alwaysVisibleStars = someStarInfo();
        final List<StarInfo> sometimesVisibleStars = someStarInfo();
        final int starsCount = alwaysVisibleStars.size() + sometimesVisibleStars.size();
        given(starInfoRepository.findByDeclinationLessThan(anyDouble())).willReturn(alwaysVisibleStars);
        given(starInfoRepository.findByDeclinationBetween(anyDouble(), anyDouble())).willReturn(sometimesVisibleStars);
        given(starPositionCalculator.getMaximumZenithDistance()).willReturn(50.0);
        given(starPositionCalculator.calculateCelestialPosition(any(Latitude.class), any(LocalTime.class), any(StarInfo.class)))
                .willReturn(new StarCelestialPosition(60, 0), new StarCelestialPosition(0, 0));

        // when
        final List<StarPositionDto> starsPositions = provider.getStarPositions(userLocation, arbitraryDate);

        // then
        assertThat(starsPositions).hasSize(starsCount - 1);
        verify(siderealHourAngleCalculator).siderealHourAngle(arbitraryDate, userLocation.getLongitude());
        verify(starPositionCalculator).getMaximumZenithDistance();
        verify(starPositionCalculator, times(starsCount))
                .calculateCelestialPosition(any(Latitude.class), any(LocalTime.class), any(StarInfo.class));
    }

    @Test
    public void shouldGetOnlyBrightestStars() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final ZonedDateTime arbitraryDate = ZonedDateTime.of(2014, 7, 16, 23, 29, 0, 0, ZoneId.of("UTC"));
        final List<StarInfo> starInfo = someStarInfo();
        given(starInfoRepository.findByApparentMagnitudeLessThan(anyDouble())).willReturn(starInfo);
        given(starPositionCalculator.getMaximumZenithDistance()).willReturn(90.0);
        given(starPositionCalculator.calculateCelestialPosition(any(Latitude.class), any(LocalTime.class), any(StarInfo.class)))
                .willReturn(new StarCelestialPosition(60, 0), new StarCelestialPosition(0, 0));

        // when
        final List<StarPositionDto> brightStarPositions = provider.getBrightStarPositions(userLocation, arbitraryDate,
                StarPositionProvider.BRIGHT_STAR_MAGNITUDE);

        // then
        assertThat(brightStarPositions).hasSize(starInfo.size());
    }

    private List<StarInfo> someStarInfo() {
        return Lists.newArrayList(new StarInfo(), new StarInfo());
    }

    private UserLocation userLocationWithLatitude(final double latitude) {
        return new UserLocation(new Latitude(latitude), null, null);
    }
}
