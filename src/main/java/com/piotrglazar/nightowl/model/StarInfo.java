package com.piotrglazar.nightowl.model;

import com.google.common.base.MoreObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public final class StarInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private LocalTime rightAscension;

    private double declination;

    private String spectralType;

    private String name;

    private double apparentMagnitude;

    public StarInfo() {

    }

    public StarInfo(LocalTime rightAscension, double declination, String spectralType, String name, double apparentMagnitude) {
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.spectralType = spectralType;
        this.name = name;
        this.apparentMagnitude = apparentMagnitude;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getRightAscension() {
        return rightAscension;
    }

    public double getDeclination() {
        return declination;
    }

    public String getSpectralType() {
        return spectralType;
    }

    public void setSpectralType(final String spectralType) {
        this.spectralType = spectralType;
    }

    public void setRightAscension(final LocalTime rightAscension) {
        this.rightAscension = rightAscension;
    }

    public void setDeclination(final double declination) {
        this.declination = declination;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getApparentMagnitude() {
        return apparentMagnitude;
    }

    public void setApparentMagnitude(final double apparentMagnitude) {
        this.apparentMagnitude = apparentMagnitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StarInfo other = (StarInfo) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rightAscension", rightAscension)
                .add("declination", declination)
                .add("spectralType", spectralType)
                .add("name", name)
                .add("apparentMagnitude", apparentMagnitude)
                .toString();
    }
}
