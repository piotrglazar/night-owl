package com.piotrglazar.nightowl.ui.map;

import javax.annotation.concurrent.Immutable;

@Immutable
final class StarName {
    private final Point point;
    private final String name;

    StarName(final Point point, final String name) {
        this.point = point;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Point getPoint() {
        return point;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }
}
