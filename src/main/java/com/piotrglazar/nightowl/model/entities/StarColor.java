package com.piotrglazar.nightowl.model.entities;

import java.awt.Color;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public enum StarColor {

    O(new Color(155, 176, 255)),
    B(new Color(202, 215, 255)),
    A(new Color(245, 245, 255)),
    F(new Color(255, 255, 208)),
    G(new Color(255, 255, 80)),
    K(new Color(255, 192, 16)),
    M(new Color(255, 136, 102));

    private static final Map<String, StarColor> BY_SPECTRAL_TYPE = Arrays.stream(StarColor.values())
            .collect(toMap(c -> c.name().substring(0, 1), c -> c));

    private final Color color;

    StarColor(final Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static StarColor fromStarSpectralType(String spectralType) {
        return BY_SPECTRAL_TYPE.get(spectralType.substring(0, 1));
    }
}
