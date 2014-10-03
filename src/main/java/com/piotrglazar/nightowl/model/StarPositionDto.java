package com.piotrglazar.nightowl.model;

import com.google.common.base.MoreObjects;
import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import com.piotrglazar.nightowl.model.entities.StarInfo;

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

    public double getAzimuth() {
        return starCelestialPosition.getAzimuth();
    }

    public String getName() {
        return starInfo.getName();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("starInfo", starInfo)
                .add("starCelestialPosition", starCelestialPosition)
                .toString();
    }
}
