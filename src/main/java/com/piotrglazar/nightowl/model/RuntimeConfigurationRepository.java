package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuntimeConfigurationRepository extends JpaRepository<RuntimeConfiguration, Long> {
}
