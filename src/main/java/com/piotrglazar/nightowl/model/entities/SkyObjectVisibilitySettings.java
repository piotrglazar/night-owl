package com.piotrglazar.nightowl.model.entities;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public final class SkyObjectVisibilitySettings {

    public static final double minimalStarVisibilityMag = -2.0;
    public static final double maximalStarVisibilityMag = 10.0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double starVisibilityMag;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Double getStarVisibilityMag() {
        return starVisibilityMag;
    }

    public void setStarVisibilityMag(final Double starVisibilityMag) {
        this.starVisibilityMag = starVisibilityMag;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SkyObjectVisibilitySettings other = (SkyObjectVisibilitySettings) obj;
        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("starVisibilityMag", starVisibilityMag)
                .toString();
    }
}
