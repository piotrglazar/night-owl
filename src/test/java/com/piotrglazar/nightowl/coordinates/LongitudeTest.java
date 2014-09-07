package com.piotrglazar.nightowl.coordinates;

import com.piotrglazar.nightowl.converters.LocalTimeConverter;
import com.piotrglazar.nightowl.converters.LongitudeConverter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConvertParam;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class LongitudeTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLongitudeLessThanMinus180() {
        // expect
        new Longitude(-181);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLongitudeGreaterThan180() {
        // expect
        new Longitude(180.1);
    }

    @Test
    public void shouldProperlyConstructLongitude() {
        // when
        final Longitude longitude = new Longitude(21);

        // then
        assertThat(longitude.getLongitude()).isEqualTo(21);
    }

    @Test
    @Parameters({
            "21.0 | 01:24:00",
            "0.0 | 00:00:00",
            "150.0 | 10:00:00",
            "180.0 | 12:00:00"
    })
    public void shouldCalculateHourShiftEastwards(@ConvertParam(LongitudeConverter.class) Longitude longitude,
                                                  @ConvertParam(LocalTimeConverter.class) LocalTime expectedTimeShift) {
        // when
        final int shiftInSeconds = longitude.toSeconds();
        final LocalTime timeShift = LocalTime.ofSecondOfDay(shiftInSeconds);

        // then
        assertThat(timeShift).isEqualTo(expectedTimeShift);
        assertThat(shiftInSeconds).isGreaterThanOrEqualTo(0);
    }

    @Test
    @Parameters({
            "-21.0 | 01:24:00",
            "-0.0 | 00:00:00",
            "-150.0 | 10:00:00",
            "-180.0 | 12:00:00"
    })
    public void shouldCalculateHourShiftWestwards(@ConvertParam(LongitudeConverter.class) Longitude longitude,
                                                  @ConvertParam(LocalTimeConverter.class) LocalTime expectedTimeShift) {
        // when
        final int shiftInSeconds = longitude.toSeconds();
        final LocalTime timeShift = LocalTime.ofSecondOfDay(-shiftInSeconds);

        // then
        assertThat(timeShift).isEqualTo(expectedTimeShift);
        assertThat(shiftInSeconds).isLessThanOrEqualTo(0);
    }

    @Test
    @Parameters({
            "-21.0 | 21.00 W",
            "0.0 | 0.00",
            "10.0 | 10.00 E"
    })
    public void shouldUseEastOrWestSuffix(double rawLongitude, String expectedToString) {
        // given
        final Longitude longitude = new Longitude(rawLongitude);

        // when
        final String toString = longitude.toString();

        // then
        assertThat(toString).isEqualTo(expectedToString);
    }

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(Longitude.class).verify();
    }
}
