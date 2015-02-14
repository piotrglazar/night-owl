package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.ui.SkyMapRotations;
import com.piotrglazar.nightowl.ui.SkyMapZoom;
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
    private final SkyMapZoom skyMapZoom;

    @Autowired
    public SkyMapPreprocessor(SkyMapRotations skyMapRotations, SkyMapZoom skyMapZoom) {
        this.skyMapRotations = skyMapRotations;
        this.skyMapZoom = skyMapZoom;
    }

    public void preProcess(Graphics graphics, SkyMapPreprocessingContext context) {
        final Graphics2D graphics2d = (Graphics2D) graphics;
        setAntiAliasing(graphics2d);
        affineTransformations(context, graphics2d);
    }

    private void affineTransformations(SkyMapPreprocessingContext context, Graphics2D graphics2d) {
        final AffineTransform affineTransform = graphics2d.getTransform();
        scale(context, affineTransform);
        move(affineTransform);
        rotate(context, affineTransform);
        graphics2d.setTransform(affineTransform);
    }

    private void move(AffineTransform affineTransform) {
        affineTransform.translate(skyMapZoom.getShiftX(), skyMapZoom.getShiftY());
    }

    private void scale(SkyMapPreprocessingContext context, AffineTransform affineTransform) {
        final double scale = skyMapZoom.getScale();
        final double deltaX = (scale - 1.0) * context.getMapCenterX();
        final double deltaY = (scale - 1.0) * context.getMapCenterY();
        affineTransform.translate(-deltaX, -deltaY);
        affineTransform.scale(scale, scale);
    }

    private void rotate(SkyMapPreprocessingContext context, AffineTransform affineTransform) {
        affineTransform.rotate(skyMapRotations.getRotationRadians(), context.getMapCenterX(), context.getMapCenterY());
    }

    private void setAntiAliasing(Graphics2D graphics2d) {
        graphics2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
    }
}
