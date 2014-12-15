package com.piotrglazar.nightowl;

import com.piotrglazar.nightowl.api.DatabasePopulator;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
import com.piotrglazar.nightowl.model.SkyObjectVisibilitySettingsRepository;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class DatabaseTestPopulator implements DatabasePopulator {

    private final UserLocationRepository userLocationRepository;
    private final RuntimeConfigurationRepository runtimeConfigurationRepository;
    private final StarInfoRepository starInfoRepository;
    private final SkyObjectVisibilitySettingsRepository settingsRepository;

    @Autowired
    public DatabaseTestPopulator(UserLocationRepository userLocationRepository,
                                 RuntimeConfigurationRepository runtimeConfigurationRepository,
                                 StarInfoRepository starInfoRepository, SkyObjectVisibilitySettingsRepository settingsRepository) {
        this.userLocationRepository = userLocationRepository;
        this.runtimeConfigurationRepository = runtimeConfigurationRepository;
        this.starInfoRepository = starInfoRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void prepareDatabase() {
        saveSomeStarInfo();
        prepareUserLocalisation();
    }

    private void prepareUserLocalisation() {
        final UserLocation warsaw = warsaw();
        userLocationRepository.saveAndFlush(warsaw);

        final SkyObjectVisibilitySettings visibilitySettings = defaultVisibilitySettings();
        settingsRepository.saveAndFlush(visibilitySettings);

        runtimeConfigurationRepository.saveAndFlush(defaultRuntimeConfiguration(warsaw, visibilitySettings));
    }

    private void saveSomeStarInfo() {
        starInfoRepository.save(DatabaseTestConfiguration.STARS);
        starInfoRepository.flush();
    }

    private RuntimeConfiguration defaultRuntimeConfiguration(final UserLocation defaultLocation,
                                                             final SkyObjectVisibilitySettings visibilitySettings) {
        final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();
        runtimeConfiguration.setChosenUserLocation(defaultLocation);
        runtimeConfiguration.setVisibilitySettings(visibilitySettings);
        return runtimeConfiguration;
    }

    private SkyObjectVisibilitySettings defaultVisibilitySettings() {
        final SkyObjectVisibilitySettings skyObjectVisibilitySettings = new SkyObjectVisibilitySettings();
        skyObjectVisibilitySettings.setShowStarLabels(true);
        skyObjectVisibilitySettings.setStarVisibilityMag(0.0);
        return skyObjectVisibilitySettings;
    }

    private UserLocation warsaw() {
        return UserLocation.builder()
                .latitude(new Latitude(52.23))
                .longitude(new Longitude(21.0))
                .name("Warsaw")
                .build();
    }
}
