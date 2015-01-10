package com.piotrglazar.nightowl.ui.map;

import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.Graphics;


@Component
public class SkyMapCircle {

    public void draw(Graphics graphics, int x, int y, int radius) {
        int topX = x - radius;
        int topY = y - radius;
        drawOval(graphics, radius, topX, topY);
    }

    public void fillWithColor(Graphics graphics, int x, int y, int radius, Color color) {
        int topX = x - radius;
        int topY = y - radius;

        if (radius > 0) {
            final Color oldColor = graphics.getColor();
            graphics.setColor(color);

            graphics.fillOval(topX, topY, radius * 2, radius * 2);
            graphics.setColor(oldColor);
        }

        drawOval(graphics, radius, topX, topY);
    }

    private void drawOval(Graphics graphics, int radius, int topX, int topY) {
        graphics.drawOval(topX, topY, radius * 2, radius * 2);
    }
}
