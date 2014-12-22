package com.piotrglazar.nightowl.model.entities;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public final class SkyObjectVisibilitySettings {

    public static final double MINIMAL_STAR_VISIBILITY_MAG = -2.0;
    public static final double MAXIMAL_STAR_VISIBILITY_MAG = 10.0;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private double starVisibilityMag;

    @Column(nullable = false)
    private boolean showStarLabels;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public double getStarVisibilityMag() {
        return starVisibilityMag;
    }

    public void setStarVisibilityMag(final double starVisibilityMag) {
        this.starVisibilityMag = starVisibilityMag;
    }

    public void setShowStarLabels(final boolean showStarLabels) {
        this.showStarLabels = showStarLabels;
    }

    public boolean getShowStarLabels() {
        return showStarLabels;
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
                .add("showStarLabels", showStarLabels)
                .toString();
    }
}
