package com.piotrglazar.nightowl.ui;

import org.springframework.stereotype.Component;

@Component
public class SkyMapZoom {

    // we use a power of 2 (negative in this case) to avoid precision loss due to floating point operations
    private static final double SCALE_FACTOR = 0.0625;
    private static final int STEP = 25;

    private double scale = 1;
    private int shiftX = 0;
    private int shiftY = 0;

    public void zoomIn() {
        scale += SCALE_FACTOR;
    }

    public void zoomOut() {
        scale -= SCALE_FACTOR;
    }

    public double getScale() {
        return scale;
    }

    public void moveUp() {
        shiftY -= STEP;
    }

    public void moveDown() {
        shiftY += STEP;
    }

    public void moveLeft() {
        shiftX -= STEP;
    }

    public void moveRight() {
        shiftX += STEP;
    }

    public int getShiftX() {
        return shiftX;
    }

    public int getShiftY() {
        return shiftY;
    }

    public void reset() {
        scale = 1.0;
        shiftX = 0;
        shiftY = 0;
    }
}
