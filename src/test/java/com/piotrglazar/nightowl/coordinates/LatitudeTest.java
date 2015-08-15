package com.piotrglazar.nightowl.coordinates;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class LatitudeTest {

    @Test(expected = IllegalArgumentException.class)
    @Parameters({
            "-91",
            "92"
    })
    public void shouldThrowExceptionWhenLongitudeIsOutOfBoundaries(double latitude) {
        // expect
        new Latitude(latitude);
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

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(Latitude.class).verify();
    }
}
