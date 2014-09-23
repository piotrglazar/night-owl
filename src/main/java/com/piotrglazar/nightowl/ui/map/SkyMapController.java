package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.logic.StarPositionProvider;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.util.StateReloadEvent;
import com.piotrglazar.nightowl.util.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.lang.invoke.MethodHandles;

@Component
public class SkyMapController implements ApplicationListener<StateReloadEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NightOwlRuntimeConfiguration runtimeConfiguration;
    private final SkyMap skyMap;
    private final StarPositionProvider starPositionProvider;
    private final TimeProvider timeProvider;
    private java.util.List<StarPositionDto> starPositions;

    @Autowired
    public SkyMapController(NightOwlRuntimeConfiguration runtimeConfiguration, SkyMap skyMap, StarPositionProvider starPositionProvider,
                            TimeProvider timeProvider) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.skyMap = skyMap;
        this.starPositionProvider = starPositionProvider;
        this.timeProvider = timeProvider;
    }

    public void draw(Graphics graphics, int width, int height) {
        int radius = calculateMapRadius(width, height);
        int x = panelCenter(width);
        int y = panelCenter(height);
        double azimuthDistance = azimuthDistance();
        final java.util.List<StarPositionDto> starPositions = getStarPositions();

        final SkyMapDto skyMapDto = new SkyMapDtoBuilder()
                                            .radius(radius)
                                            .x(x)
                                            .y(y)
                                            .azimuthDistance(azimuthDistance)
                                            .starPositions(starPositions)
                                            .build();
        skyMap.draw(graphics, skyMapDto);
    }

    private java.util.List<StarPositionDto> getStarPositions() {
        if (starPositions == null) {
            starPositions = starPositionProvider.getBrightStarPositions(runtimeConfiguration.getUserLocation(), timeProvider.get());
        }
        return starPositions;
    }

    public double azimuthDistance() {
        final UserLocation userLocation = runtimeConfiguration.getUserLocation();
        return userLocation.getLatitude().getLatitude();
    }

    private int panelCenter(final int dimension) {
        return dimension / 2;
    }

    private int calculateMapRadius(final int width, final int height) {
        return Math.min(width, height) / 2;
    }

    @Override
    public void onApplicationEvent(final StateReloadEvent event) {
        LOG.info("Reloading star positions {}", event);

        starPositions = starPositionProvider.getBrightStarPositions(runtimeConfiguration.getUserLocation(), timeProvider.get());
    }
}
