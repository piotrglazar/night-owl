package com.piotrglazar.nightowl.ui.map;

import java.awt.Graphics;

public enum CardinalDirections {

    N {
        @Override
        public void draw(final Graphics graphics, final int circleX, final int circleY, final int radius) {
            int y = circleY - radius;
            int[] xs = {circleX - HALF_WIDTH, circleX + HALF_WIDTH, circleX + HALF_WIDTH, circleX - HALF_WIDTH};
            int[] ys = {y, y, y + HEIGHT, y + HEIGHT};

            graphics.fillPolygon(xs, ys, 4);
            final int height = graphics.getFontMetrics().getHeight();
            graphics.drawString("N", circleX - HALF_WIDTH, y + height);
        }
    },
    S {
        @Override
        public void draw(final Graphics graphics, final int circleX, final int circleY, final int radius) {
            int y = circleY + radius;
            int[] xs = {circleX - HALF_WIDTH, circleX + HALF_WIDTH, circleX + HALF_WIDTH, circleX - HALF_WIDTH};
            int[] ys = {y - HEIGHT, y - HEIGHT, y, y};

            graphics.fillPolygon(xs, ys, 4);
            graphics.drawString("S", circleX - WIDTH, y - HEIGHT - 2);
        }
    },
    W {
        @Override
        public void draw(final Graphics graphics, final int circleX, final int circleY, final int radius) {
            int x = circleX + radius;
            int[] xs = {x - HEIGHT, x, x, x - HEIGHT};
            int[] ys = {circleY - HALF_WIDTH, circleY - HALF_WIDTH, circleY + HALF_WIDTH, circleY + HALF_WIDTH};

            graphics.fillPolygon(xs, ys, 4);
            graphics.drawString("W", x - 2 * HEIGHT - 2, circleY + HALF_WIDTH);
        }
    },
    E {
        @Override
        public void draw(final Graphics graphics, final int circleX, final int circleY, final int radius) {
            int x = circleX - radius;
            int[] xs = {x, x + HEIGHT, x + HEIGHT, x};
            int[] ys = {circleY - HALF_WIDTH, circleY - HALF_WIDTH, circleY + HALF_WIDTH, circleY + HALF_WIDTH};

            graphics.fillPolygon(xs, ys, 4);
            graphics.drawString("E", x + HEIGHT, circleY + HALF_HEIGHT);
        }
    };

    private static final int WIDTH = 2;
    private static final int HALF_WIDTH = WIDTH / 2;
    private static final int HEIGHT = 6;
    private static final int HALF_HEIGHT = HEIGHT / 2;

    public abstract void draw(Graphics graphics, int x, int y, int radius);
}
