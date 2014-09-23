package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.StarInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DefaultStarInfoProvider implements StarInfoProvider {

    private final StarInfoRepository starInfoRepository;

    @Autowired
    DefaultStarInfoProvider(final StarInfoRepository starInfoRepository) {
        this.starInfoRepository = starInfoRepository;
    }

    @Override
    public void saveStarInfo(final StarInfo starInfo) {
        starInfoRepository.saveAndFlush(starInfo);
    }

    @Override
    public List<StarInfo> getAllStars() {
        return starInfoRepository.findAll();
    }

    @Override
    public long count() {
        return starInfoRepository.count();
    }

    @Override
    public void deleteAll() {
        starInfoRepository.deleteAll();
    }

    @Override
    public List<StarInfo> getStarsWithDeclinationGreaterThan(final double declination) {
        return starInfoRepository.findByDeclinationGreaterThan(declination);
    }

    @Override
    public List<StarInfo> getStarsWithDeclinationLessThan(final double declination) {
        return starInfoRepository.findByDeclinationLessThan(declination);
    }

    @Override
    public List<StarInfo> getStarsWithDeclinationBetween(final double lowerBoundary, final double upperBoundary) {
        return starInfoRepository.findByDeclinationBetween(lowerBoundary, upperBoundary);
    }

    @Override
    public List<StarInfo> getStarsBrighterThan(final double apparentMagnitude) {
        return starInfoRepository.findByApparentMagnitudeLessThan(apparentMagnitude);
    }
}
