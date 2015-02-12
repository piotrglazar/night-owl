package com.piotrglazar.nightowl.ui.map;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class SkyMapPreprocessingContext {

    private final int mapCenterX;
    private final int mapCenterY;

    public SkyMapPreprocessingContext(int mapCenterX, int mapCenterY) {
        this.mapCenterX = mapCenterX;
        this.mapCenterY = mapCenterY;
    }

    public int getMapCenterX() {
        return mapCenterX;
    }

    public int getMapCenterY() {
        return mapCenterY;
    }
}
