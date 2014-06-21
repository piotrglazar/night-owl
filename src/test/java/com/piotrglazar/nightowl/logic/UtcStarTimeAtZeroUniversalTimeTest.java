package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.converters.LocalDateConverter;
import com.piotrglazar.nightowl.converters.LocalDateTimeConverter;
import com.piotrglazar.nightowl.converters.LocalTimeConverter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConvertParam;
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
    public void shouldCalculateAriesPoint(@ConvertParam(LocalDateConverter.class) LocalDate date,
                                          @ConvertParam(LocalDateConverter.class) LocalDate expectedAriesPoint) {
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
    public void shouldCalculateUtcStarTimeAtZeroUniversalTime(@ConvertParam(LocalDateTimeConverter.class) LocalDateTime date,
                                                              @ConvertParam(LocalTimeConverter.class) LocalTime expectedTime) {
        // when
        final LocalTime calculatedTime = time.getTime(date);

        // then
        assertThat(calculatedTime).isEqualTo(expectedTime);
    }
}
