package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.api.DatabaseFromScriptReader;
import com.piotrglazar.nightowl.api.DatabasePopulator;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.logic.StarPositionProvider;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
import com.piotrglazar.nightowl.model.SkyObjectVisibilitySettingsRepository;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@Profile("default")
public class DefaultDatabasePopulator implements DatabasePopulator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserLocationRepository userLocationRepository;
    private final RuntimeConfigurationRepository runtimeConfigurationRepository;
    private final SkyObjectVisibilitySettingsRepository skyObjectVisibilitySettingsRepository;
    private final DatabaseFromScriptReader databaseFromScriptReader;

    @Autowired
    public DefaultDatabasePopulator(UserLocationRepository userLocation, RuntimeConfigurationRepository runtimeConfiguration,
                                    SkyObjectVisibilitySettingsRepository skyObjectVisibilitySettingsRepository,
                                    DatabaseFromScriptReader databaseFromScriptReader) {
        this.userLocationRepository = userLocation;
        this.runtimeConfigurationRepository = runtimeConfiguration;
        this.skyObjectVisibilitySettingsRepository = skyObjectVisibilitySettingsRepository;
        this.databaseFromScriptReader = databaseFromScriptReader;
    }

    @Override
    public void prepareDatabase() {
        LOG.info("Prepopulating database");

        databaseFromScriptReader.createDatabaseFromScript();

        final UserLocation preferredUserLocation = prepareCapitals();
        final SkyObjectVisibilitySettings skyObjectVisibilitySettings = prepareSkyObjectsVisibility();
        prepareRuntimeConfiguration(preferredUserLocation, skyObjectVisibilitySettings);

        LOG.info("Done prepopulating database");
    }

    private void prepareRuntimeConfiguration(UserLocation preferredUserLocation,
                                             SkyObjectVisibilitySettings skyObjectVisibilitySettings) {
        if (runtimeConfigurationRepository.count() == 0) {
            runtimeConfigurationRepository.saveAndFlush(defaultRuntimeConfiguration(preferredUserLocation, skyObjectVisibilitySettings));
        }
    }

    private SkyObjectVisibilitySettings prepareSkyObjectsVisibility() {
        if (skyObjectVisibilitySettingsRepository.count() == 0) {
            SkyObjectVisibilitySettings skyObjectVisibilitySettings = new SkyObjectVisibilitySettings();
            skyObjectVisibilitySettings.setStarVisibilityMag(StarPositionProvider.BRIGHT_STAR_MAGNITUDE);
            skyObjectVisibilitySettings.setShowStarLabels(true);
            return skyObjectVisibilitySettingsRepository.saveAndFlush(skyObjectVisibilitySettings);
        } else {
            return skyObjectVisibilitySettingsRepository.findAll().get(0);
        }
    }

    private UserLocation prepareCapitals() {
        if (userLocationRepository.count() == 0) {
            LOG.info("Prepopulating database with capitals");

            final UserLocation warsaw = warsaw();
            userLocationRepository.save(warsaw);
            userLocationRepository.save(london());
            userLocationRepository.save(sydney());
            userLocationRepository.flush();

            return warsaw;
        } else {
            return userLocationRepository.findAll().get(0);
        }
    }

    private RuntimeConfiguration defaultRuntimeConfiguration(UserLocation defaultLocation, SkyObjectVisibilitySettings
            skyObjectVisibilitySettings) {
        final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();
        runtimeConfiguration.setChosenUserLocation(defaultLocation);
        runtimeConfiguration.setVisibilitySettings(skyObjectVisibilitySettings);
        return runtimeConfiguration;
    }

    private UserLocation sydney() {
        return UserLocation.builder()
                .latitude(new Latitude(-34.0))
                .longitude(new Longitude(151.0))
                .name("Sydney")
                .build();
    }

    private UserLocation london() {
        return UserLocation.builder()
                .latitude(new Latitude(51.51))
                .longitude(new Longitude(-0.13))
                .name("London")
                .build();
    }

    private UserLocation warsaw() {
        return UserLocation.builder()
                .latitude(new Latitude(52.23))
                .longitude(new Longitude(21.0))
                .name("Warsaw")
                .build();
    }
}
