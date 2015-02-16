package com.piotrglazar.nightowl.ui;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SkyMapRotationsTest {

    private SkyMapRotations skyMapRotations = new SkyMapRotations();

    @Test
    public void shouldRotateRightByGivenStep() {
        // when
        skyMapRotations.rotateRight();

        // then
        assertThat(skyMapRotations.getRotationRadians()).isEqualTo(Math.toRadians(12));
    }

    @Test
    public void shouldRotateLeftByGivenStep() {
        // when
        skyMapRotations.rotateLeft();

        // then
        assertThat(skyMapRotations.getRotationRadians()).isEqualTo(Math.toRadians(-12));
    }

    @Test
    public void shouldRotateRightByFullCircle() {
        // when
        IntStream.range(0, 30).forEach(i -> skyMapRotations.rotateRight());

        // then
        assertThat(skyMapRotations.getRotationRadians()).isEqualTo(0);
    }

    @Test
    public void shouldRotateLeftByFullCircle() {
        // when
        IntStream.range(0, 30).forEach(i -> skyMapRotations.rotateLeft());

        // then
        assertThat(skyMapRotations.getRotationRadians()).isEqualTo(0);
    }
}
