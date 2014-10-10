package com.piotrglazar.nightowl.model.entities;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;

/**
 * This should be a singleton!
 */
@Entity
public final class RuntimeConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private UserLocation chosenUserLocation;

    @OneToOne
    private SkyObjectVisibilitySettings visibilitySettings;

    public Long getId() {
        return id;
    }

    public UserLocation getChosenUserLocation() {
        return chosenUserLocation;
    }

    public void setChosenUserLocation(final UserLocation chosenUserLocation) {
        this.chosenUserLocation = chosenUserLocation;
    }

    public SkyObjectVisibilitySettings getVisibilitySettings() {
        return visibilitySettings;
    }

    public void setVisibilitySettings(final SkyObjectVisibilitySettings visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RuntimeConfiguration)) {
            return false;
        }

        final RuntimeConfiguration that = (RuntimeConfiguration) o;

        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userLocation", chosenUserLocation)
                .add("visibilitySettings", visibilitySettings)
                .toString();
    }
}
