package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkyObjectVisibilitySettingsRepository extends JpaRepository<SkyObjectVisibilitySettings, Long> {
}
