package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.logic.StarPositionCalculator;
import com.piotrglazar.nightowl.model.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;

@Component
public class SkyMap {

    private final SkyMapCircle skyMapCircle;
    private final SkyMapDot skyMapDot;
    private java.util.List<DirectionsSign> directionsSigns;
    private final NightOwlRuntimeConfiguration runtimeConfiguration;
    private final StarPositionCalculator starPositionCalculator;

    @Autowired
    public SkyMap(SkyMapCircle skyMapCircle, SkyMapDot skyMapDot, NightOwlRuntimeConfiguration runtimeConfiguration,
                  StarPositionCalculator starPositionCalculator) {
        this.skyMapCircle = skyMapCircle;
        this.skyMapDot = skyMapDot;
        this.runtimeConfiguration = runtimeConfiguration;
        this.starPositionCalculator = starPositionCalculator;
        directionsSigns = Arrays.asList(DirectionsSign.values());
    }

    public void setDirectionsSigns(final java.util.List<DirectionsSign> directionsSigns) {
        this.directionsSigns = directionsSigns;
    }

    public void draw(Graphics graphics, int width, int height) {
        int radius = calculateMapRadius(width, height);
        int x = panelCenter(width);
        int y = panelCenter(height);
        skyMapCircle.draw(graphics, x, y, radius);
        skyMapDot.draw(graphics, x, y);
        directionsSigns.forEach(s -> s.draw(graphics, x, y, radius));
        int pole = calculatePole(y, radius);
        skyMapDot.draw(graphics, x, pole);
    }

    private int calculatePole(final int y, final int radius) {
        final UserLocation userLocation = runtimeConfiguration.getUserLocation();
        final double distanceFromPoleInAngles = starPositionCalculator.poleCompletion(userLocation.getLatitude().getLatitude());
        return y - (int) Math.round(distanceFromPoleInAngles * radius / Latitude.MAXIMUM_ABS_LATITUDE);
    }

    private int panelCenter(final int dimension) {
        return dimension / 2;
    }

    private int calculateMapRadius(final int width, final int height) {
        return Math.min(width, height) / 2;
    }
}
