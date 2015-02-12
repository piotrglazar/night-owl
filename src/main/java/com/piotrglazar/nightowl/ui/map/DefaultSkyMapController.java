package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.api.SkyMapController;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.logic.StarPositionProvider;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Graphics;
import java.util.List;

@Component
public class DefaultSkyMapController implements SkyMapController {

    private final NightOwlRuntimeConfiguration runtimeConfiguration;
    private final SkyMap skyMap;
    private final StarPositionProvider starPositionProvider;
    private final TimeProvider timeProvider;
    private final SkyMapPreprocessor skyMapPreprocessor;

    @Autowired
    public DefaultSkyMapController(NightOwlRuntimeConfiguration runtimeConfiguration, SkyMap skyMap,
                                   StarPositionProvider starPositionProvider, TimeProvider timeProvider,
                                   SkyMapPreprocessor skyMapPreprocessor) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.skyMap = skyMap;
        this.starPositionProvider = starPositionProvider;
        this.timeProvider = timeProvider;
        this.skyMapPreprocessor = skyMapPreprocessor;
    }

    @Override
    public void draw(Graphics graphics, int width, int height) {
        int radius = calculateMapRadius(width, height);
        int x = panelCenter(width);
        int y = panelCenter(height);
        double azimuthDistance = azimuthDistance();
        final List<StarPositionDto> starPositions = getStarPositions();

        final SkyMapDto skyMapDto = new SkyMapDtoBuilder()
                                            .radius(radius)
                                            .x(x)
                                            .y(y)
                                            .azimuthDistance(azimuthDistance)
                                            .starPositions(starPositions)
                                            .skyDisplayContext(runtimeConfiguration.skyDisplayContext())
                                            .build();

        skyMapPreprocessor.preProcess(graphics, new SkyMapPreprocessingContext(x, y));
        skyMap.draw(graphics, skyMapDto);
    }

    private List<StarPositionDto> getStarPositions() {
        return starPositionProvider.getBrightStarPositionsCached(runtimeConfiguration.getUserLocation(), timeProvider.get(),
                runtimeConfiguration.getStarVisibilityMagnitude());
    }

    @Override
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
}
