package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.ui.SkyMapRotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

@Component
public class SkyMapPreprocessor {

    private final SkyMapRotations skyMapRotations;

    @Autowired
    public SkyMapPreprocessor(SkyMapRotations skyMapRotations) {
        this.skyMapRotations = skyMapRotations;
    }

    public void preProcess(Graphics graphics, SkyMapPreprocessingContext context) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        setAntiAliasing(graphics2d);
        rotate(context, graphics2d);
    }

    private void rotate(SkyMapPreprocessingContext context, Graphics2D graphics2d) {
        AffineTransform affineTransform = graphics2d.getTransform();
        affineTransform.rotate(skyMapRotations.getRotationRadians(), context.getMapCenterX(), context.getMapCenterY());
        graphics2d.setTransform(affineTransform);
    }

    private void setAntiAliasing(Graphics2D graphics2d) {
        graphics2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
    }
}
