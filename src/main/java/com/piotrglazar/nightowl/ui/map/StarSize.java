package com.piotrglazar.nightowl.ui.map;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class StarSize {

    private final int size;

    public StarSize(final int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
