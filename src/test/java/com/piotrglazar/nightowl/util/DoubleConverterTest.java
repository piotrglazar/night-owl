package com.piotrglazar.nightowl.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DoubleConverterTest {

    private DoubleConverter doubleConverter = new DoubleConverter();

    @Test
    public void shouldConvertStringRepresentationToDouble() {
        // given
        final String value = "1.234";

        // when
        final double doubleValue = doubleConverter.twoDecimalPlaces(value);

        // then
        assertThat(doubleValue).isEqualTo(1.23);
    }

    @Test
    @Parameters({
            "1.2345 | 1.23",
            "1.235 | 1.24",
            "-1.23 | -1.23",
            "1.999 | 2.0"
    })
    public void shouldTrimValueToTwoDecimalPlacesWithRounding(double value, double expectedResult) {
        // when
        final double result = doubleConverter.twoDecimalPlaces(value);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }
}
