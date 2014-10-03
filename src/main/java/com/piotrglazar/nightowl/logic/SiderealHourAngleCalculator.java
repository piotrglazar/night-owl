package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.coordinates.Longitude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class SiderealHourAngleCalculator {

    private final TimeConverter timeConverter;

    private final UtcStarTimeAtZeroUniversalTime zeroUniversalTime;

    @Autowired
    public SiderealHourAngleCalculator(TimeConverter timeConverter, UtcStarTimeAtZeroUniversalTime zeroUniversalTime) {
        this.timeConverter = timeConverter;
        this.zeroUniversalTime = zeroUniversalTime;
    }

    public LocalTime siderealHourAngle(final ZonedDateTime time, final Longitude longitude) {
        final LocalTime siderealHourAngleAtGreenwich = siderealHourAngleAtGreenwich(time);
        return siderealHourAngleAtGreenwich.plusSeconds(longitude.toSeconds());
    }

    public LocalTime siderealHourAngleAtGreenwich(final ZonedDateTime time) {
        final ZonedDateTime utc = time.withZoneSameInstant(ZoneId.of("UTC"));

        final ZonedDateTime utcStarTime = timeConverter.solarToStar(utc);
        final LocalTime utcStarTimeAtZeroUniversalTime = zeroUniversalTime.getTime(utc.toLocalDateTime());

        return utcStarTime.toLocalTime().plusSeconds(utcStarTimeAtZeroUniversalTime.toSecondOfDay());
    }
}
