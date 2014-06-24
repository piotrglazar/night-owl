package com.piotrglazar.nightowl.coordinates;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class LatitudeTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLongitudeLessThanMinus90() {
        // expect
        new Latitude(-91);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLongitudeGreaterThan90() {
        // expect
        new Latitude(92);
    }

    @Test
    public void shouldConstructLatitude() {
        // when
        final Latitude latitude = new Latitude(52);

        // then
        assertThat(latitude.getLatitude()).isEqualTo(52);
    }

    @Test
    @Parameters({
            "35.0 | 35.00 N",
            "0.0 | 0.00",
            "-12.3 | 12.30 S"
    })
    public void shouldUseNorthOrSouthSuffix(double rawLatitude, String expectedToString) {
        // given
        final Latitude latitude = new Latitude(rawLatitude);

        // when
        final String toString = latitude.toString();

        // then
        assertThat(toString).isEqualTo(expectedToString);
    }
}
