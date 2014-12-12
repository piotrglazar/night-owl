package com.piotrglazar.nightowl.model.entities;

import java.time.LocalTime;

public class StarInfoBuilder {

    private LocalTime rightAscension = LocalTime.of(0, 0);
    private double declination;
    private String spectralType = "";
    private StarInfoDetails starInfoDetails;
    private double apparentMagnitude;

    public StarInfoBuilder rightAscension(final LocalTime rightAscension) {
        this.rightAscension = rightAscension;
        return this;
    }

    public StarInfoBuilder declination(final double declination) {
        this.declination = declination;
        return this;
    }

    public StarInfoBuilder spectralType(final String spectralType) {
        this.spectralType = spectralType;
        return this;
    }

    public StarInfoBuilder name(final String name) {
        this.starInfoDetails = new StarInfoDetails(name);
        return this;
    }

    public StarInfoBuilder apparentMagnitude(final double apparentMagnitude) {
        this.apparentMagnitude = apparentMagnitude;
        return this;
    }

    public StarInfo build() {
        return new StarInfo(rightAscension, declination, spectralType, starInfoDetails, apparentMagnitude);
    }
}
