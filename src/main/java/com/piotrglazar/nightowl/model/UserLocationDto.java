package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;

public class UserLocationDto {

    private final Latitude latitude;
    private final Longitude longitude;
    private final String name;
    private final Long id;

    public UserLocationDto(final Latitude latitude, final Longitude longitude, final String name, final Long id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.id = id;
    }

    public static UserLocationDto fromUserLocation(UserLocation userLocation) {
        return new UserLocationDto(userLocation.getLatitude(), userLocation.getLongitude(), userLocation.getName(), userLocation.getId());
    }

    public Latitude getLatitude() {
        return latitude;
    }

    public Longitude getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", name, longitude, latitude);
    }
}
