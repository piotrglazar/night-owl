package com.piotrglazar.nightowl;

import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;

public interface RuntimeConfigurationProvider {

    RuntimeConfiguration getConfiguration();

    void updateConfiguration(RuntimeConfiguration runtimeConfiguration);
}
