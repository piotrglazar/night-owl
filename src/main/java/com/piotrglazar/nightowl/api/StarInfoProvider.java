package com.piotrglazar.nightowl.api;

import com.piotrglazar.nightowl.model.entities.StarInfo;

import java.util.List;

public interface StarInfoProvider {

    void saveStarInfo(StarInfo starInfo);

    List<StarInfo> getAllStars();

    long count();

    void deleteAll();

    List<StarInfo> getStarsWithDeclinationGreaterThan(double declination);

    List<StarInfo> getStarsWithDeclinationLessThan(double declination);

    List<StarInfo> getStarsWithDeclinationBetween(double lowerBoundary, double upperBoundary);

    List<StarInfo> getStarsBrighterThan(double apparentMagnitude);
}