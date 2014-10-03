package com.piotrglazar.nightowl.util.messages;

import java.util.Objects;

public final class StarsVisibilityMessage {

    private final long starsAlwaysVisible;
    private final long starsSometimesVisible;
    private final long starsNeverVisible;

    public StarsVisibilityMessage(long starsAlwaysVisible, long starsSometimesVisible, long starsNeverVisible) {
        this.starsAlwaysVisible = starsAlwaysVisible;
        this.starsSometimesVisible = starsSometimesVisible;
        this.starsNeverVisible = starsNeverVisible;
    }

    public long getStarsAlwaysVisible() {
        return starsAlwaysVisible;
    }

    public long getStarsSometimesVisible() {
        return starsSometimesVisible;
    }

    public long getStarsNeverVisible() {
        return starsNeverVisible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(starsAlwaysVisible, starsSometimesVisible, starsNeverVisible);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StarsVisibilityMessage other = (StarsVisibilityMessage) obj;
        return Objects.equals(this.starsAlwaysVisible, other.starsAlwaysVisible) &&
                Objects.equals(this.starsSometimesVisible, other.starsSometimesVisible) &&
                Objects.equals(this.starsNeverVisible, other.starsNeverVisible);
    }
}
