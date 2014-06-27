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
}
