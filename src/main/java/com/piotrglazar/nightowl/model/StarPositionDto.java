package com.piotrglazar.nightowl.model;

import com.google.common.base.MoreObjects;

public class StarPositionDto {

    private final StarInfo starInfo;
    private final StarCelestialPosition starCelestialPosition;

    public StarPositionDto(final StarInfo starInfo, final StarCelestialPosition starCelestialPosition) {
        this.starInfo = starInfo;
        this.starCelestialPosition = starCelestialPosition;
    }

    public StarInfo getStarInfo() {
        return starInfo;
    }

    public StarCelestialPosition getStarCelestialPosition() {
        return starCelestialPosition;
    }

    public double getZenithDistance() {
        return starCelestialPosition.getZenithDistance();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("starInfo", starInfo)
                .add("starCelestialPosition", starCelestialPosition)
                .toString();
    }
}
