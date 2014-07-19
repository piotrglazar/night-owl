package com.piotrglazar.nightowl.model;

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

    public StarInfo() {

    }

    public StarInfo(LocalTime rightAscension, double declination, String spectralType) {
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.spectralType = spectralType;
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
        return com.google.common.base.Objects.toStringHelper(this)
                .add("rightAscension", rightAscension)
                .add("declination", declination)
                .add("spectralType", spectralType)
                .toString();
    }
}
