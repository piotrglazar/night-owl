package com.piotrglazar.nightowl;

import java.awt.*;

public interface SkyMapController {
    void draw(Graphics graphics, int width, int height);

    double azimuthDistance();
}
