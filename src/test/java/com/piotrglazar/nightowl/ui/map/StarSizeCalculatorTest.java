package com.piotrglazar.nightowl.ui.map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class StarSizeCalculatorTest {

    private StarSizeCalculator starSizeCalculator = new StarSizeCalculator("1.0 = 3, 3.0 = 2, 5.0 = 1");

    @Test
    public void shouldUseDefaultSmallestStarSize() {
        // given
        final StarSizeCalculator starSizeCalculator = new StarSizeCalculator("");
        final double arbitraryMagnitude = 1.0;

        // when
        final StarSize starSize = starSizeCalculator.calculateStarSize(arbitraryMagnitude);

        // then
        assertThat(starSize).isEqualTo(StarSizeCalculator.SMALLEST_STAR);
    }

    @Test
    @Parameters({
            "-1.0 | 3",
            "0.0 | 3",
            "2.0 | 2",
            "2.5 | 2",
            "4.5 | 1",
            "5.0 | 0",
            "7.0 | 0",
    })
    public void shouldFindStarSizeBasedOnMagnitude(double magnitude, int expectedStarSize) {
        // when
        final StarSize starSize = starSizeCalculator.calculateStarSize(magnitude);

        // then
        assertThat(starSize.getSize()).isEqualTo(expectedStarSize);
    }
}
