package com.piotrglazar.nightowl.ui.map;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.model.StarPositionDto;

import java.util.List;

public class SkyMapDtoBuilder {

    private int radius;
    private int x;
    private int y;
    private double azimuthDistance;
    private List<StarPositionDto> starPositions = Lists.newLinkedList();

    public SkyMapDtoBuilder radius(final int radius) {
        this.radius = radius;
        return this;
    }

    public SkyMapDtoBuilder x(final int x) {
        this.x = x;
        return this;
    }

    public SkyMapDtoBuilder y(final int y) {
        this.y = y;
        return this;
    }

    public SkyMapDtoBuilder azimuthDistance(final double azimuthDistance) {
        this.azimuthDistance = azimuthDistance;
        return this;
    }

    public SkyMapDtoBuilder starPositions(final List<StarPositionDto> starPositions) {
        this.starPositions = starPositions;
        return this;
    }

    public SkyMapDto build() {
        return new SkyMapDto(radius, x, y, azimuthDistance, starPositions);
    }
}
