package com.piotrglazar.nightowl;

import com.piotrglazar.nightowl.model.StarInfo;

import java.util.List;

public interface StarInfoProvider {

    void saveStarInfo(StarInfo starInfo);

    List<StarInfo> getAllStars();

    long count();

    void deleteAll();

    List<StarInfo> getStarsWithDeclinationGreaterThan(double declination);

    List<StarInfo> getStarsWithDeclinationLessThan(double declination);

    List<StarInfo> getStarsWithDeclinationBetween(double lowerBoundary, double upperBounday);
}
