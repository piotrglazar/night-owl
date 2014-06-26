package com.piotrglazar.nightowl;

import com.piotrglazar.nightowl.model.StarInfo;

import java.util.List;

public interface StarInfoProvider {

    void saveStarInfo(StarInfo starInfo);

    List<StarInfo> getAllStars();

    long count();
}
