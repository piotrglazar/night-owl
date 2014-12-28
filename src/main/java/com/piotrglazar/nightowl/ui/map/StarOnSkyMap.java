package com.piotrglazar.nightowl.ui.map;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class StarOnSkyMap {

    private final Point point;
    private final StarSize starSize;

    public StarOnSkyMap(Point point, StarSize starSize) {
        this.point = point;
        this.starSize = starSize;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }

    public int getSize() {
        return starSize.getSize();
    }
}
