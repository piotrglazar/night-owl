package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.AbstractContextTest;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.UserLocation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class StarPositionProviderContextTest extends AbstractContextTest {

    @Autowired
    private StarPositionProvider starPositionProvider;

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

    private UserLocation userLocationWithLatitude(final double latitude) {
        return new UserLocation(new Latitude(latitude), null, null);
    }
}
