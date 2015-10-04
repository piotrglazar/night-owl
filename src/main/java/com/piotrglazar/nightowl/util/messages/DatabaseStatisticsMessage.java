package com.piotrglazar.nightowl.util.messages;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class DatabaseStatisticsMessage {

    private final long numberOfStars;
    private final long numberOfLocations;
    private final long numberOfStarsVisibleNow;
    private final long numberOfStarsAlwaysVisible;
    private final long numberOfStarsNeverVisible;
    private final String locationName;
    private final double locationLatitude;
    private final double locationLongitude;

    public DatabaseStatisticsMessage(long numberOfStars, long numberOfLocations, long numberOfStarsVisibleNow,
                                     long numberOfStarsAlwaysVisible, long numberOfStarsNeverVisible, String locationName,
                                     double locationLatitude, double locationLongitude) {
        this.numberOfStars = numberOfStars;
        this.numberOfLocations = numberOfLocations;
        this.numberOfStarsVisibleNow = numberOfStarsVisibleNow;
        this.numberOfStarsAlwaysVisible = numberOfStarsAlwaysVisible;
        this.numberOfStarsNeverVisible = numberOfStarsNeverVisible;
        this.locationName = locationName;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public long getNumberOfStars() {
        return numberOfStars;
    }

    public long getNumberOfLocations() {
        return numberOfLocations;
    }

    public long getNumberOfStarsVisibleNow() {
        return numberOfStarsVisibleNow;
    }

    public long getNumberOfStarsAlwaysVisible() {
        return numberOfStarsAlwaysVisible;
    }

    public long getNumberOfStarsNeverVisible() {
        return numberOfStarsNeverVisible;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }
}
