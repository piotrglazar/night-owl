package com.piotrglazar.nightowl.model.entities;

import com.google.common.base.MoreObjects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

@Entity
public final class StarInfo {

    @Id
    @GeneratedValue
    protected Long id;

    @Column(nullable = false)
    private LocalTime rightAscension;

    @Column(nullable = false)
    private double declination;

    @Column(nullable = false)
    private String spectralType;

    @Column(nullable = false)
    private StarColor starColor;

    @OneToOne(cascade = CascadeType.ALL)
    private StarInfoDetails starInfoDetails;

    @Column(nullable = false)
    private double apparentMagnitude;

    public StarInfo() {

    }

    public StarInfo(LocalTime rightAscension, double declination, String spectralType, StarInfoDetails starInfoDetails,
                    double apparentMagnitude, StarColor starColor) {
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.spectralType = spectralType;
        this.starInfoDetails = starInfoDetails;
        this.apparentMagnitude = apparentMagnitude;
        this.starColor = starColor;
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

    public Optional<StarInfoDetails> getStarInfoDetails() {
        return Optional.ofNullable(starInfoDetails);
    }

    public void setName(final StarInfoDetails starInfoDetails) {
        this.starInfoDetails = starInfoDetails;
    }

    public double getApparentMagnitude() {
        return apparentMagnitude;
    }

    public void setApparentMagnitude(final double apparentMagnitude) {
        this.apparentMagnitude = apparentMagnitude;
    }

    public StarColor getStarColor() {
        return starColor;
    }

    public void setStarColor(final StarColor starColor) {
        this.starColor = starColor;
    }

    public void setStarInfoDetails(final StarInfoDetails starInfoDetails) {
        this.starInfoDetails = starInfoDetails;
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
                .add("starInfoDetails", starInfoDetails)
                .add("apparentMagnitude", apparentMagnitude)
                .add("starColor", starColor)
                .toString();
    }
}
