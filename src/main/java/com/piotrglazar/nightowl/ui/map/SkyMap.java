package com.piotrglazar.nightowl.ui.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

@Component
public class SkyMap {

    private final SkyMapCircle skyMapCircle;
    private final SkyMapDot skyMapDot;
    private final SkyMapCalculations skyMapCalculations;
    private java.util.List<CardinalDirections> cardinalDirections;

    @Autowired
    public SkyMap(SkyMapCircle skyMapCircle, SkyMapDot skyMapDot, SkyMapCalculations skyMapCalculations) {
        this.skyMapCircle = skyMapCircle;
        this.skyMapDot = skyMapDot;
        this.skyMapCalculations = skyMapCalculations;
        this.cardinalDirections = Arrays.asList(CardinalDirections.values());
    }

    public void setCardinalDirections(final java.util.List<CardinalDirections> cardinalDirections) {
        this.cardinalDirections = cardinalDirections;
    }

    public void draw(final Graphics graphics, final SkyMapDto skyMap) {
        skyMapCircle.draw(graphics, skyMap.getX(), skyMap.getY(), skyMap.getRadius());
        skyMapDot.draw(graphics, skyMap.getX(), skyMap.getY());
        cardinalDirections.forEach(s -> s.draw(graphics, skyMap.getX(), skyMap.getY(), skyMap.getRadius()));
        final int pole = skyMapCalculations.distanceFromCenter(skyMap.getY(), skyMap.getRadius(), skyMap.getAzimuthDistance());
        skyMapDot.draw(graphics, skyMap.getX(), pole);

        drawStarPositions(skyMap, graphics);
    }

    private void drawStarPositions(final SkyMapDto skyMap, final Graphics graphics) {
        final int x = skyMap.getX();
        final int y = skyMap.getY();
        final java.util.List<PointAndName> pointAndNames = skyMap.getStarPositions().stream()
                .map(p -> new PointAndName(skyMapCalculations.starLocation(x, y, skyMap.getRadius(), p.getAzimuth(), p.getZenithDistance()),
                        p.getName())).collect(toList());

        drawStars(graphics, x, pointAndNames);
        drawStarLabelsIfNecessary(skyMap, graphics, x, pointAndNames);
    }

    private void drawStarLabelsIfNecessary(SkyMapDto skyMap, Graphics graphics, int x, java.util.List<PointAndName> pointAndNames) {
        if (skyMap.getSkyDisplayContext().shouldShowStarLabels()) {
            pointAndNames.forEach(p -> graphics.drawString(p.getName(), mirrorXCoordinate(p.getX(), x), p.getY()));
        }
    }

    private void drawStars(Graphics graphics, int x, java.util.List<PointAndName> pointAndNames) {
        pointAndNames.forEach(p -> graphics.fillRect(mirrorXCoordinate(p.getX(), x), p.getY(), 1, 1));
    }

    public int mirrorXCoordinate(final int pointX, final int x) {
        final int delta = x - pointX;
        return x + delta;
    }

    private static class PointAndName {
        private final Point point;
        private final String name;

        private PointAndName(final Point point, final String name) {
            this.point = point;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Point getPoint() {
            return point;
        }

        public int getX() {
            return point.getX();
        }

        public int getY() {
            return point.getY();
        }
    }
}
