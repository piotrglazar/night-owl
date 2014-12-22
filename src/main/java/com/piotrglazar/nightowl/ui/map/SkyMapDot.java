package com.piotrglazar.nightowl.ui.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Graphics;

@Component
public class SkyMapDot {

    public static final int RADIUS = 1;

    private final SkyMapCircle skyMapCircle;

    @Autowired
    public SkyMapDot(SkyMapCircle skyMapCircle) {
        this.skyMapCircle = skyMapCircle;
    }

    public void draw(Graphics graphics, int x, int y) {
        skyMapCircle.draw(graphics, x, y, RADIUS);
    }
}
