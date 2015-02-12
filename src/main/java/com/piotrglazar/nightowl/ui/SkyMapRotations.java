package com.piotrglazar.nightowl.ui;

import org.springframework.stereotype.Component;

@Component
public class SkyMapRotations {

    private static final int FULL_CIRCLE_DEGREES = 360;
    private static final int STEP_DEGREES = 12;
    private static final int MAX_STEPS = FULL_CIRCLE_DEGREES / STEP_DEGREES;

    private int degreesRotated = 0;

    public void rotateLeft() {
        degreesRotated = (degreesRotated - STEP_DEGREES) % MAX_STEPS;
    }

    public void rotateRight() {
        degreesRotated = (degreesRotated + STEP_DEGREES) % MAX_STEPS;
    }

    public double getRotationRadians() {
        return Math.toRadians(degreesRotated);
    }
}
