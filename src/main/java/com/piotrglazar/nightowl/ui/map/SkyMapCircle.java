package com.piotrglazar.nightowl.ui.map;

import org.springframework.stereotype.Component;

import java.awt.Graphics;

@Component
public class SkyMapCircle {

    public void draw(Graphics graphics, int x, int y, int radius) {
        int topX = x - radius;
        int topY = y - radius;
        graphics.drawOval(topX, topY, radius * 2, radius * 2);
    }
}
