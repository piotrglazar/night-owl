package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.api.RuntimeConfigurationProvider;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

@Component
class DefaultRuntimeConfigurationProvider implements RuntimeConfigurationProvider {

    private final RuntimeConfigurationRepository configurationRepository;
    private final SkyObjectVisibilitySettingsRepository settingsRepository;

    @Autowired
    public DefaultRuntimeConfigurationProvider(RuntimeConfigurationRepository configurationRepository,
                                               SkyObjectVisibilitySettingsRepository settingsRepository) {
        this.configurationRepository = configurationRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public RuntimeConfiguration getConfiguration() {
        final List<RuntimeConfiguration> runtimeConfigurations = configurationRepository.findAll();
        checkState(runtimeConfigurations.size() == 1, "There must be exactly one configuration, got " + runtimeConfigurations.size());
        return runtimeConfigurations.get(0);
    }

    @Override
    public void updateConfiguration(final RuntimeConfiguration runtimeConfiguration) {
        settingsRepository.save(runtimeConfiguration.getVisibilitySettings());
        configurationRepository.save(runtimeConfiguration);
    }
}
