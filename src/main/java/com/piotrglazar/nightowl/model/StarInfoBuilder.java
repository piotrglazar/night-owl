package com.piotrglazar.nightowl.model;

import java.time.LocalTime;

public class StarInfoBuilder {

    private LocalTime rightAscension = LocalTime.of(0, 0);
    private double declination;
    private String spectralType = "";
    private String name = "";
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
        this.name = name;
        return this;
    }

    public StarInfoBuilder apparentMagnitude(final double apparentMagnitude) {
        this.apparentMagnitude = apparentMagnitude;
        return this;
    }

    public StarInfo build() {
        return new StarInfo(rightAscension, declination, spectralType, name, apparentMagnitude);
    }
}
