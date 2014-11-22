package com.piotrglazar.nightowl.model;

import com.google.common.base.Preconditions;
import com.piotrglazar.nightowl.api.RuntimeConfigurationProvider;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("databasePopulator")
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
        Preconditions.checkState(runtimeConfigurations.size() == 1, "There must be exactly one configuration");
        return runtimeConfigurations.get(0);
    }

    @Override
    public void updateConfiguration(final RuntimeConfiguration runtimeConfiguration) {
        settingsRepository.save(runtimeConfiguration.getVisibilitySettings());
        configurationRepository.save(runtimeConfiguration);
    }
}
