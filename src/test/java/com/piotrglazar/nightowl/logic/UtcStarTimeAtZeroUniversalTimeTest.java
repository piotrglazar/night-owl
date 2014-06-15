package com.piotrglazar.nightowl.logic;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class UtcStarTimeAtZeroUniversalTimeTest {

    private UtcStarTimeAtZeroUniversalTime time = new UtcStarTimeAtZeroUniversalTime();

    @Test
    @Parameters({
            "2014-11-21 | 2014-09-21",
            "2014-09-21 | 2014-09-21",
            "2014-09-20 | 2013-09-21",
            "2014-05-15 | 2013-09-21"
    })
    public void shouldCalculateAriesPoint(String currentDate, String ariesPoint) {
        // given
        final LocalDate date = LocalDate.parse(currentDate);
        final LocalDate expectedAriesPoint = LocalDate.parse(ariesPoint);

        // when
        final LocalDate calculatedAriesPoint = time.getAriesPoint(date);

        // then
        assertThat(calculatedAriesPoint).isEqualTo(expectedAriesPoint);
    }

    @Test
    @Parameters({
            "2014-11-21 | 04:03:52",
            "2014-09-21 | 00:00:00",
            "2014-08-21 | 21:57:40"
    })
    public void shouldCalculateUtcStarTimeAtZeroUniversalTime(String currentDate, String rawExpectedTime) {
        // given
        final LocalDateTime date = LocalDateTime.of(LocalDate.parse(currentDate), LocalTime.now());
        final LocalTime expectedTime = LocalTime.parse(rawExpectedTime);

        // when
        final LocalTime calculatedTime = time.getTime(date);

        // then
        assertThat(calculatedTime).isEqualTo(expectedTime);
    }
}
