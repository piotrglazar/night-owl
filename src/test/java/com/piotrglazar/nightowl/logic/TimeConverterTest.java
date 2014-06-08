package com.piotrglazar.nightowl.logic;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TimeConverterTest {

    private TimeConverter timeConverter = new TimeConverter();

    @Test
    @Parameters({
            "2014-08-06T14:45:25Z | 00:02:25",
            "2014-08-06T00:00:00Z | 00:00:00",
            "2014-08-06T00:01:00Z | 00:00:00",
            "2014-08-06T00:07:00Z | 00:00:01",
            "2014-08-06T22:59:59Z | 00:03:46"
    })
    public void shouldConvertSolarTimeToStarTime(String rawTime, String rawExpectedTimeDelta) {
        // given
        final ZonedDateTime utc = ZonedDateTime.parse(rawTime);
        final LocalTime expectedTimeDelta = LocalTime.parse(rawExpectedTimeDelta);

        // when
        final ZonedDateTime star = timeConverter.solarToStar(utc);

        // then
        assertThat(star.isBefore(utc)).isFalse();
        final LocalTime timeDelta = timeDelta(utc, star);
        assertThat(timeDelta).isEqualTo(expectedTimeDelta);
    }

    private LocalTime timeDelta(final ZonedDateTime utc, final ZonedDateTime star) {
        final int solarSecondsOfDay = utc.toLocalTime().toSecondOfDay();
        final int starSecondsOfDay = star.toLocalTime().toSecondOfDay();
        return LocalTime.ofSecondOfDay(starSecondsOfDay - solarSecondsOfDay);
    }
}
