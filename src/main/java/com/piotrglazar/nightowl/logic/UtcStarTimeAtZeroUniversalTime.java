package com.piotrglazar.nightowl.logic;

import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class UtcStarTimeAtZeroUniversalTime {

    public static final Month ARIES_POINT_MONTH = Month.SEPTEMBER;

    public static final int ARIES_POINT_DAY = 21;

    private static final LocalTime SHIFT = LocalTime.of(0, 3, 56);

    public LocalTime getTime(final LocalDateTime currentDate) {
        final LocalDateTime ariesPoint = LocalDateTime.of(getAriesPoint(currentDate.toLocalDate()), LocalTime.MIDNIGHT);
        final long daysDelta = daysDelta(currentDate, ariesPoint);
        return LocalTime.ofSecondOfDay(SHIFT.toSecondOfDay() * daysDelta);
    }

    private long daysDelta(final LocalDateTime currentDate, final LocalDateTime ariesPoint) {
        if (ariesPoint.toLocalDate().equals(currentDate.toLocalDate())) {
            return 0;
        } else {
            final Duration duration = Duration.between(ariesPoint, currentDate.plusDays(1));
            return duration.toDays();
        }
    }

    public LocalDate getAriesPoint(LocalDate currentDate) {
        final int ariesPointYear = getAriesPointYear(currentDate);
        final LocalDate ariesPoint = LocalDate.of(ariesPointYear, ARIES_POINT_MONTH, ARIES_POINT_DAY);
        return ariesPoint;
    }

    private int getAriesPointYear(final LocalDate currentDate) {
        if (isCurrentDatePastOrEqualAriesPoint(currentDate)) {
            return currentDate.getYear();
        } else {
            return currentDate.getYear() - 1;
        }
    }

    private boolean isCurrentDatePastOrEqualAriesPoint(final LocalDate currentDate) {
        final Month currentMonth = currentDate.getMonth();
        if (currentMonth.compareTo(ARIES_POINT_MONTH) < 0) {
            return false;
        } else if (currentMonth.compareTo(ARIES_POINT_MONTH) == 0 && currentDate.getDayOfMonth() < ARIES_POINT_DAY) {
            return false;
        } else if (currentMonth.compareTo(ARIES_POINT_MONTH) == 0 && currentDate.getDayOfMonth() >= ARIES_POINT_DAY) {
            return true;
        } else {
            return true;
        }
    }
}
