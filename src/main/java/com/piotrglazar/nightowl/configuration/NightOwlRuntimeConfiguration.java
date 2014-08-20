package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.RuntimeConfigurationProvider;
import com.piotrglazar.nightowl.model.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.UserLocation;
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
    private RuntimeConfiguration runtimeConfiguration;

    @Autowired
    public NightOwlRuntimeConfiguration(ApplicationEventPublisher applicationEventPublisher,
                                        RuntimeConfigurationProvider configurationProvider) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.configurationProvider = configurationProvider;
    }

    @PostConstruct
    public void loadRuntimeConfiguration() {
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

    public void updateUserLocation(UserLocation newUserLocation) {
        LOG.info("Updating runtime configuration - new user location");

        runtimeConfiguration.setChosenUserLocation(newUserLocation);
        configurationProvider.updateConfiguration(runtimeConfiguration);

        applicationEventPublisher.publishEvent(new UiUpdateEvent(this, ui -> ui.setUserLocation(newUserLocation)));

        LOG.info("Finished updating runtime configuration - new user location");
    }
}
