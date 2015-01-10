package com.piotrglazar.nightowl.ui.map;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

@Component
public class SkyMap {

    private final SkyMapCircle skyMapCircle;
    private final SkyMapDot skyMapDot;
    private final SkyMapCalculations skyMapCalculations;
    private List<CardinalDirections> cardinalDirections;

    @Autowired
    public SkyMap(SkyMapCircle skyMapCircle, SkyMapDot skyMapDot, SkyMapCalculations skyMapCalculations) {
        this.skyMapCircle = skyMapCircle;
        this.skyMapDot = skyMapDot;
        this.skyMapCalculations = skyMapCalculations;
        this.cardinalDirections = Arrays.asList(CardinalDirections.values());
    }

    public void setCardinalDirections(final List<CardinalDirections> cardinalDirections) {
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
        final List<StarOnSkyMap> stars = Lists.newLinkedList();
        final List<StarName> starNames = Lists.newLinkedList();

        skyMap.getStarPositions().forEach(p -> {
            final Point starLocation = skyMapCalculations.starLocation(x, y, skyMap.getRadius(), p.getAzimuth(), p.getZenithDistance());
            stars.add(new StarOnSkyMap(starLocation, skyMapCalculations.starSize(p.getApparentMagnitude()), p.getStarColor()));
            p.getName().ifPresent(starName -> starNames.add(new StarName(starLocation, starName)));
        });

        drawStars(graphics, x, stars);
        drawStarNames(skyMap, graphics, x, starNames);
    }

    private void drawStarNames(SkyMapDto skyMap, Graphics graphics, int x, List<StarName> starNames) {
        if (skyMap.getSkyDisplayContext().shouldShowStarLabels()) {
            starNames.stream().forEach(p -> graphics.drawString(p.getName(), mirrorXCoordinate(p.getX(), x), p.getY()));
        }
    }

    private void drawStars(Graphics graphics, int x, List<StarOnSkyMap> stars) {
        stars.forEach(p -> skyMapCircle.fillWithColor(graphics, mirrorXCoordinate(p.getX(), x), p.getY(), p.getSize(), p.getStarColor()));
    }

    public int mirrorXCoordinate(final int pointX, final int x) {
        final int delta = x - pointX;
        return x + delta;
    }
}
