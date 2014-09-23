package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.model.StarPositionDto;

import java.util.List;

public class SkyMapDto {

    private final int radius;
    private final int x;
    private final int y;
    private final double azimuthDistance;
    private final List<StarPositionDto> starPositions;

    public SkyMapDto(final int radius, final int x, final int y, final double azimuthDistance, final List<StarPositionDto> starPositions) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.azimuthDistance = azimuthDistance;
        this.starPositions = starPositions;
    }

    public int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAzimuthDistance() {
        return azimuthDistance;
    }

    public List<StarPositionDto> getStarPositions() {
        return starPositions;
    }
}
