package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;

public class UserLocationBuilder {

    private Latitude latitude = new Latitude(0);
    private Longitude longitude = new Longitude(0);
    private String name = "default";

    public UserLocationBuilder latitude(final Latitude latitude) {
        this.latitude = latitude;
        return this;
    }

    public UserLocationBuilder longitude(final Longitude longitude) {
        this.longitude = longitude;
        return this;
    }

    public UserLocationBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public UserLocation build() {
        return new UserLocation(latitude, longitude, name);
    }
}
