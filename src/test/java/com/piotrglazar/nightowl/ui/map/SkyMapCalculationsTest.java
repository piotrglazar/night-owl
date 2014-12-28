package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.logic.StarPositionCalculator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class SkyMapCalculationsTest {

    @Mock
    private StarPositionCalculator starPositionCalculator;

    @Mock
    private StarSizeCalculator starSizeCalculator;

    @InjectMocks
    private SkyMapCalculations skyMapCalculations;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters({
            "0, 0, 600, 45.0, 45.0 | 212, -212",
            "0, 0, 600, 45.0, 30.0 | 141, -141",
            "20, 20, 600, 45.0, 45.0 | 232, -192",
            "20, 20, 600, 45.0, 30.0 | 161, -121"
    })
    public void shouldCalculateObjectPosition(int x, int y, int radius, double azimuth, double zenithDistance, int expectedX,
                                              int expectedY) {
        // when
        final Point point = skyMapCalculations.starLocation(x, y, radius, azimuth, zenithDistance);

        // then
        assertThat(point.getX()).isEqualTo(expectedX);
        assertThat(point.getY()).isEqualTo(expectedY);
    }

    @Test
    @Parameters({
            "45.0 | 25",
            "20.0 | 39",
            "75.0 | 8",
            "-45.0 | 75",
            "-20.0 | 61",
            "-75.0 | 92"
    })
    public void shouldCalculateSkyPoleLocation(double latitude, int expectedPole) {
        // given
        int arbitraryWidthAndHeight = 100;
        int mapCenterY = arbitraryWidthAndHeight / 2;
        given(starPositionCalculator.poleCompletion(latitude)).willReturn(latitude);

        // when
        final int distanceFromCenter = skyMapCalculations.distanceFromCenter(mapCenterY, mapCenterY, latitude);

        // then
        assertThat(distanceFromCenter).isEqualTo(expectedPole);
    }

    @Test
    public void shouldUseStarSizeCalculator() {
        // given
        double arbitraryMagnitude = 1.0;
        final StarSize arbitraryStarSize = new StarSize(3);
        given(starSizeCalculator.calculateStarSize(arbitraryMagnitude)).willReturn(arbitraryStarSize);

        // when
        StarSize starSize = skyMapCalculations.starSize(arbitraryMagnitude);

        // then
        assertThat(starSize).isEqualTo(arbitraryStarSize);
    }
}
