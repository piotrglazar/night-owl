package com.piotrglazar.nightowl.util;

import com.google.common.base.MoreObjects;

public class StateReloadEvent implements NightOwlEvent {

    private final String cause;

    public StateReloadEvent(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cause", getCause())
                .toString();
    }
}
