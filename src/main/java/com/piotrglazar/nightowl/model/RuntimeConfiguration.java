package com.piotrglazar.nightowl.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

/**
 * This should be a singleton!
 */
@Entity
public class RuntimeConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private UserLocation chosenUserLocation;

    public Long getId() {
        return id;
    }

    public UserLocation getChosenUserLocation() {
        return chosenUserLocation;
    }

    public void setChosenUserLocation(final UserLocation chosenUserLocation) {
        this.chosenUserLocation = chosenUserLocation;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;

        if (!(o instanceof RuntimeConfiguration))
            return false;

        final RuntimeConfiguration that = (RuntimeConfiguration) o;

        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
