package com.piotrglazar.nightowl.logic;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.StarInfo;
import com.piotrglazar.nightowl.model.UserLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StarPositionProviderTest {

    @Mock
    private StarInfoProvider starInfoProvider;

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
        given(starInfoProvider.getStarsWithDeclinationGreaterThan(declination)).willReturn(starInfo);

        // when
        final long numberOfStarsAlwaysVisible = provider.getNumberOfStarsAlwaysVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(52.0);
        verify(starInfoProvider).getStarsWithDeclinationGreaterThan(declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoProvider);
        assertThat(numberOfStarsAlwaysVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetSometimesVisibleStarsCountForNorth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final double declination = 40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(52.0)).willReturn(declination);
        given(starInfoProvider.getStarsWithDeclinationBetween(-declination, declination)).willReturn(starInfo);

        // when
        final long numberOfStarsSometimesVisible = provider.getNumberOfStarsSometimesVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(52.0);
        verify(starInfoProvider).getStarsWithDeclinationBetween(-declination, declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoProvider);
        assertThat(numberOfStarsSometimesVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetNeverVisibleStarsCountForNorth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(52.0);
        final double declination = 40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(52.0)).willReturn(declination);
        given(starInfoProvider.getStarsWithDeclinationLessThan(-declination)).willReturn(starInfo);

        // when
        final long numberOfStarsNeverVisible = provider.getNumberOfStarsNeverVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(52.0);
        verify(starInfoProvider).getStarsWithDeclinationLessThan(-declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoProvider);
        assertThat(numberOfStarsNeverVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetAlwaysVisibleStarsCountForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);
        final double declination = -40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(-52.0)).willReturn(declination);
        given(starInfoProvider.getStarsWithDeclinationLessThan(declination)).willReturn(starInfo);

        // when
        final long numberOfStarsAlwaysVisible = provider.getNumberOfStarsAlwaysVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(-52.0);
        verify(starInfoProvider).getStarsWithDeclinationLessThan(declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoProvider);
        assertThat(numberOfStarsAlwaysVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetSometimesVisibleStarsCountForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);
        final double declination = -40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(-52.0)).willReturn(declination);
        given(starInfoProvider.getStarsWithDeclinationBetween(declination, -declination)).willReturn(starInfo);

        // when
        final long numberOfStarsSometimesVisible = provider.getNumberOfStarsSometimesVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(-52.0);
        verify(starInfoProvider).getStarsWithDeclinationBetween(declination, -declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoProvider);
        assertThat(numberOfStarsSometimesVisible).isEqualTo(starInfo.size());
    }

    @Test
    public void shouldGetNumberOfStarsNeverVisibleForSouth() {
        // given
        final UserLocation userLocation = userLocationWithLatitude(-52.0);
        final double declination = -40.0;
        final List<StarInfo> starInfo = someStarInfo();
        given(starPositionCalculator.calculateBoundaryDeclination(-52.0)).willReturn(declination);
        given(starInfoProvider.getStarsWithDeclinationGreaterThan(-declination)).willReturn(starInfo);

        // when
        final long numberOfStarsNeverVisible = provider.getNumberOfStarsNeverVisible(userLocation);

        // then
        verify(starPositionCalculator).calculateBoundaryDeclination(-52.0);
        verify(starInfoProvider).getStarsWithDeclinationGreaterThan(-declination);
        verifyNoMoreInteractions(starPositionCalculator, starInfoProvider);
        assertThat(numberOfStarsNeverVisible).isEqualTo(starInfo.size());
    }

    private List<StarInfo> someStarInfo() {
        return Lists.newArrayList(new StarInfo(), new StarInfo());
    }

    private UserLocation userLocationWithLatitude(final double latitude) {
        return new UserLocation(new Latitude(latitude), null, null);
    }
}
