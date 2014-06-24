package com.piotrglazar.nightowl.model;

import com.google.common.base.Preconditions;
import com.piotrglazar.nightowl.RuntimeConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("databasePopulator")
class DefaultRuntimeConfigurationProvider implements RuntimeConfigurationProvider {

    private final RuntimeConfigurationRepository configurationRepository;

    @Autowired
    public DefaultRuntimeConfigurationProvider(final RuntimeConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public RuntimeConfiguration getConfiguration() {
        final List<RuntimeConfiguration> runtimeConfigurations = configurationRepository.findAll();
        Preconditions.checkState(runtimeConfigurations.size() == 1, "There must be exactly one configuration");
        return runtimeConfigurations.get(0);
    }
}
