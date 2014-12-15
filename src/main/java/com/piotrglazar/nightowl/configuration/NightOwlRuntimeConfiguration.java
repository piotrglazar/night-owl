package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.api.DatabasePopulator;
import com.piotrglazar.nightowl.api.RuntimeConfigurationProvider;
import com.piotrglazar.nightowl.model.SkyDisplayContext;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Component
public class NightOwlRuntimeConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationEventPublisher applicationEventPublisher;
    private final RuntimeConfigurationProvider configurationProvider;
    private final DatabasePopulator databasePopulator;
    private RuntimeConfiguration runtimeConfiguration;

    @Autowired
    public NightOwlRuntimeConfiguration(ApplicationEventPublisher applicationEventPublisher,
                                        RuntimeConfigurationProvider configurationProvider,
                                        DatabasePopulator databasePopulator) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.configurationProvider = configurationProvider;
        this.databasePopulator = databasePopulator;
    }

    @PostConstruct
    public void loadRuntimeConfiguration() {
        databasePopulator.prepareDatabase();
        runtimeConfiguration = configurationProvider.getConfiguration();
        LOG.info("Running with configuration {}", runtimeConfiguration);

        final UserLocation chosenUserLocation = runtimeConfiguration.getChosenUserLocation();
        applicationEventPublisher.publishEvent(new UiUpdateEvent(this, ui -> ui.setUserLocation(chosenUserLocation)));
    }

    public RuntimeConfiguration getRuntimeConfiguration() {
        return runtimeConfiguration;
    }

    public UserLocation getUserLocation() {
        return runtimeConfiguration.getChosenUserLocation();
    }

    public double getStarVisibilityMagnitude() {
        return runtimeConfiguration.getVisibilitySettings().getStarVisibilityMag();
    }

    public SkyDisplayContext skyDisplayContext() {
        final SkyObjectVisibilitySettings visibilitySettings = runtimeConfiguration.getVisibilitySettings();
        return new SkyDisplayContext(visibilitySettings.getStarVisibilityMag(), visibilitySettings.getShowStarLabels());
    }

    public void updateUserLocation(UserLocation newUserLocation) {
        LOG.info("Updating runtime configuration - new user location");

        runtimeConfiguration.setChosenUserLocation(newUserLocation);
        configurationProvider.updateConfiguration(runtimeConfiguration);

        applicationEventPublisher.publishEvent(new UiUpdateEvent(this, ui -> ui.setUserLocation(newUserLocation)));

        LOG.info("Finished updating runtime configuration - new user location");
    }

    public void updateStarVisibilityMagnitude(final double newStarVisibilityMag) {
        LOG.info("Updating runtime configuration - new star visibility magnitude");

        runtimeConfiguration.getVisibilitySettings().setStarVisibilityMag(newStarVisibilityMag);
        configurationProvider.updateConfiguration(runtimeConfiguration);

        LOG.info("Finished updating runtime configuration - new star visibility magnitude");
    }

    public void updateShowStarLabels(final boolean showStarLabels) {
        LOG.info("Updating runtime configuration - show star labels changed");

        runtimeConfiguration.getVisibilitySettings().setShowStarLabels(showStarLabels);
        configurationProvider.updateConfiguration(runtimeConfiguration);

        LOG.info("Finished updating runtime configuration - show star labels changed");
    }
}
