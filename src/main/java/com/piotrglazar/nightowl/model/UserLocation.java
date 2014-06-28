package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public final class UserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Latitude latitude;

    private Longitude longitude;

    private String name;

    public UserLocation(){

    }

    public UserLocation(Latitude latitude, Longitude longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Latitude getLatitude() {
        return latitude;
    }

    public void setLatitude(final Latitude latitude) {
        this.latitude = latitude;
    }

    public Longitude getLongitude() {
        return longitude;
    }

    public void setLongitude(final Longitude longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public static UserLocationBuilder builder() {
        return new UserLocationBuilder();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserLocation other = (UserLocation) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("name", name)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .toString();
    }
}
