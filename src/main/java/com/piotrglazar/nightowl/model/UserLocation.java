package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserLocation {

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
}
