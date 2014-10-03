package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.entities.UserLocation;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public final class UserLocationDto {

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

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, name, id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserLocationDto other = (UserLocationDto) obj;
        return Objects.equals(this.latitude, other.latitude)
                && Objects.equals(this.longitude, other.longitude)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.id, other.id);
    }
}
