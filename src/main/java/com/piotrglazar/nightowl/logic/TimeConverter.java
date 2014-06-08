package com.piotrglazar.nightowl.logic;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TimeConverter {

    public ZonedDateTime solarToStar(ZonedDateTime solar) {
        // we only need hours, minutes and seconds now
        final int solarSecondsOfDay = solar.toLocalTime().toSecondOfDay();

        // we can't use Duration because Duration.multiply takes long
        // and Conversion.SOLAR_TO_STAR is a double quite close to 1
        final int starSecondsOfDay = (int) (solarSecondsOfDay * Conversion.SOLAR_TO_STAR);

        final int secondsDelta = starSecondsOfDay - solarSecondsOfDay;
        return solar.plusSeconds(secondsDelta);
    }
}
