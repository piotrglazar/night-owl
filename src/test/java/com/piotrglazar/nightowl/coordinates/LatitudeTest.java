package com.piotrglazar.nightowl.coordinates;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
