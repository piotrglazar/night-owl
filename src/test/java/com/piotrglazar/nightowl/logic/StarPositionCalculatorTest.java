package com.piotrglazar.nightowl.logic;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class StarPositionCalculatorTest {

    private StarPositionCalculator starPositionCalculator = new StarPositionCalculator();

    @Test(expected = IllegalArgumentException.class)
    @Parameters({"-90", "0", "90"})
    public void shouldThrowExceptionForUnsupportedLatitude(double latitude) {
        // expect
        starPositionCalculator.calculateBoundaryDeclination(latitude);
    }

    @Test
    @Parameters({
            "52.0 | 38.0",
            "23.5 | 66.5",
            "-35.2 | -54.8",
            "-52.0 | -38.0"
    })
    public void shouldCalculateDeclinationComplementaryToPole(double latitude, double expectedDeclination) {
        // when
        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude);

        // then
        assertThat(declination).isEqualTo(expectedDeclination);
    }
}
