package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.model.entities.StarColor;

import javax.annotation.concurrent.Immutable;
import java.awt.Color;

@Immutable
public final class StarOnSkyMap {

    private final Point point;
    private final StarSize starSize;
    private final StarColor starColor;

    public StarOnSkyMap(Point point, StarSize starSize, StarColor starColor) {
        this.point = point;
        this.starSize = starSize;
        this.starColor = starColor;
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

    public Color getStarColor() {
        return starColor.getColor();
    }
}
