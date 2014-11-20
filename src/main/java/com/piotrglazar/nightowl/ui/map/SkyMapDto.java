package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.model.SkyDisplayContext;
import com.piotrglazar.nightowl.model.StarPositionDto;

import java.util.List;

public class SkyMapDto {

    private final int radius;
    private final int x;
    private final int y;
    private final double azimuthDistance;
    private final List<StarPositionDto> starPositions;
    private final SkyDisplayContext skyDisplayContext;

    public SkyMapDto(int radius, int x, int y, double azimuthDistance, List<StarPositionDto> starPositions,
                     SkyDisplayContext skyDisplayContext) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.azimuthDistance = azimuthDistance;
        this.starPositions = starPositions;
        this.skyDisplayContext = skyDisplayContext;
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

    public SkyDisplayContext getSkyDisplayContext() {
        return skyDisplayContext;
    }
}
