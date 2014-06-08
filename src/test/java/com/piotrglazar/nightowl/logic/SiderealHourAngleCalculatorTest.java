package com.piotrglazar.nightowl.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SiderealHourAngleCalculatorTest {

    @Mock
    private TimeConverter timeConverter;

    @Mock
    private UtcStarTimeAtZeroUniversalTime zeroUniversalTime;

    @InjectMocks
    private SiderealHourAngleCalculator calculator;

    @Test
    public void shouldCalculateSiderealHourAngle() {
        // given
        given(timeConverter.solarToStar(any(ZonedDateTime.class))).willReturn(ZonedDateTime.now());
        given(zeroUniversalTime.getTime(any(LocalDateTime.class))).willReturn(LocalTime.now());

        // when
        calculator.siderealHourAngleAtGreenwich(ZonedDateTime.now());

        // then
        verify(timeConverter).solarToStar(any(ZonedDateTime.class));
        verify(zeroUniversalTime).getTime(any(LocalDateTime.class));
    }
}
