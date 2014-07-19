package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.AbstractContextTest;
import com.piotrglazar.nightowl.coordinates.Longitude;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class SiderealHourAngleCalculatorContextTest extends AbstractContextTest {

    @Autowired
    private SiderealHourAngleCalculator calculator;

    @Test
    public void shouldCalculateSiderealHourAngleInGreenwich() {
        // given
        final LocalTime expectedSiderealHourAngle = LocalTime.of(22, 34, 54);
        final ZonedDateTime time = ZonedDateTime.of(2014, 11, 21, 18, 28, 0, 0, ZoneId.of("UTC"));

        // when
        final LocalTime siderealHourAngle = calculator.siderealHourAngleAtGreenwich(time);

        // then
        assertThat(siderealHourAngle).isEqualTo(expectedSiderealHourAngle);
    }

    @Test
    public void shouldCalculateSiderealHourAngleInWarsaw() {
        // given
        final LocalTime expectedSiderealHourAngle = LocalTime.of(23, 58, 54);
        final ZonedDateTime time = ZonedDateTime.of(2014, 11, 21, 18, 28, 0, 0, ZoneId.of("UTC"));
        final Longitude warsawLongitude = new Longitude(21.0);

        // when
        final LocalTime siderealHourAngleInWarsaw = calculator.siderealHourAngle(time, warsawLongitude);

        // then
        assertThat(siderealHourAngleInWarsaw).isEqualTo(expectedSiderealHourAngle);
    }

    @Test
    public void shouldCalculateSiderealHourAngleInGreenwichAtAriesPoint() {
        // given
        // the only difference comes from solar to star time conversion
        final LocalTime expectedSiderealHourAngle = LocalTime.of(12, 37, 0);
        final ZonedDateTime time = ZonedDateTime.of(2014, 9, 21, 12, 34, 56, 0, ZoneId.of("UTC"));

        // when
        final LocalTime siderealHourAngle = calculator.siderealHourAngleAtGreenwich(time);

        // then
        assertThat(siderealHourAngle).isEqualTo(expectedSiderealHourAngle);
    }
}
