package com.piotrglazar.nightowl.ui;

import com.google.common.base.Preconditions;
import com.piotrglazar.nightowl.MainWindow;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.UserLocationDto;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.DoubleConverter;
import com.piotrglazar.nightowl.util.MoreCollectors;
import com.piotrglazar.nightowl.util.StateReloadEvent;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class MainMenuController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserLocationRepository userLocationRepository;
    private final NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DoubleConverter doubleConverter;

    @Autowired
    public MainMenuController(UserLocationRepository userLocationRepository,
                              NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration,
                              ApplicationEventPublisher applicationEventPublisher, DoubleConverter doubleConverter) {
        this.userLocationRepository = userLocationRepository;
        this.nightOwlRuntimeConfiguration = nightOwlRuntimeConfiguration;
        this.applicationEventPublisher = applicationEventPublisher;
        this.doubleConverter = doubleConverter;
    }

    public List<UserLocationDto> getAllUserLocations() {
        return userLocationRepository.findAll().stream()
                .map(UserLocationDto::fromUserLocation)
                .collect(MoreCollectors.toImmutableList());
    }

    public UserLocationDto getCurrentUserLocation() {
        return UserLocationDto.fromUserLocation(nightOwlRuntimeConfiguration.getUserLocation());
    }

    public void updateUserLocation(UserLocationDto userLocationDto) {
        // cancel button was pressed, nothing to do
        if (userLocationDto == null) {
            return;
        }

        final UserLocation userLocation = userLocationRepository.findOne(userLocationDto.getId());
        Preconditions.checkState(userLocation != null, "Invalid user location: %s", userLocationDto);
        nightOwlRuntimeConfiguration.updateUserLocation(userLocation);
        updateApplication("changing user location");
    }

    public double minimalStarMagnitude() {
        return SkyObjectVisibilitySettings.minimalStarVisibilityMag;
    }

    public double maximalStarMagnitude() {
        return SkyObjectVisibilitySettings.maximalStarVisibilityMag;
    }

    public double currentStarVisibilityMagnitude() {
        return nightOwlRuntimeConfiguration.getStarVisibilityMagnitude();
    }

    public void updateStarVisibilityMagnitude(final String text) {
        try {
            final double newStarVisibilityMag = doubleConverter.twoDecimalPlaces(text);
            if (!isNewStarVisibilityMagnitudeWithinBounds(newStarVisibilityMag)) {
                LOG.error("Invalid star visibility magnitude: {}", newStarVisibilityMag);
            } else {
                nightOwlRuntimeConfiguration.updateStarVisibilityMagnitude(newStarVisibilityMag);
                updateApplication("changing star visibility settings");
            }
        } catch (NumberFormatException | NullPointerException e) {
            LOG.error("Error while parsing star visibility magnitude provided: {}", e);
        }
    }

    private void updateApplication(final String cause) {
        applicationEventPublisher.publishEvent(new StateReloadEvent(this, cause));
        applicationEventPublisher.publishEvent(new UiUpdateEvent(this, MainWindow::repaintUi));
    }

    private boolean isNewStarVisibilityMagnitudeWithinBounds(final double newStarVisibilityMag) {
        return newStarVisibilityMag >= minimalStarMagnitude() && newStarVisibilityMag <= maximalStarMagnitude();
    }
}
