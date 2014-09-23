package com.piotrglazar.nightowl.ui.map;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class Point {

    private final int x;
    private final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
