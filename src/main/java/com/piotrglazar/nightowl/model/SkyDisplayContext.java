package com.piotrglazar.nightowl.model;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class SkyDisplayContext {

    private final double starVisibilityMag;
    private final boolean showStarLabels;

    public SkyDisplayContext(double starVisibilityMag, boolean showStarLabels) {
        this.starVisibilityMag = starVisibilityMag;
        this.showStarLabels = showStarLabels;
    }

    public boolean shouldShowStarLabels() {
        return showStarLabels;
    }

    public double getStarVisibilityMag() {
        return starVisibilityMag;
    }
}
