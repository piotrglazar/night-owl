package com.piotrglazar.nightowl.ui;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class SkyMapZoomTest {

    private SkyMapZoom skyMapZoom = new SkyMapZoom();

    @Test
    @Parameters(method = "scaleOperations")
    public void shouldIncreaseScaleByStep(SkyMapZoomAction operation, double expectedScale) {
        // when
        operation.action(skyMapZoom);

        // then
        assertThat(skyMapZoom.getScale()).isEqualTo(expectedScale);
    }

    @Test
    @Parameters(method = "moveOperations")
    public void shouldMoveByStep(SkyMapZoomAction operation, int expectedShiftX, int expectedShiftY) {
        // when
        operation.action(skyMapZoom);

        // then
        assertThat(skyMapZoom.getShiftX()).isEqualTo(expectedShiftX);
        assertThat(skyMapZoom.getShiftY()).isEqualTo(expectedShiftY);
    }

    @Test
    public void shouldResetScaleAndShift() {
        // given
        skyMapZoom.zoomIn();
        skyMapZoom.moveDown();
        skyMapZoom.moveLeft();

        // when
        skyMapZoom.reset();

        // then
        assertThat(skyMapZoom.getScale()).isEqualTo(1.0);
        assertThat(skyMapZoom.getShiftX()).isEqualTo(0);
        assertThat(skyMapZoom.getShiftY()).isEqualTo(0);
    }

    public Object[] scaleOperations() {
        return $($((SkyMapZoomAction) SkyMapZoom::zoomIn, 1.0625), $((SkyMapZoomAction) SkyMapZoom::zoomOut, 0.9375));
    }

    public Object[] moveOperations() {
        return $($((SkyMapZoomAction) SkyMapZoom::moveLeft, -25, 0), $((SkyMapZoomAction) SkyMapZoom::moveRight, +25, 0),
                $((SkyMapZoomAction) SkyMapZoom::moveUp, 0, -25), $((SkyMapZoomAction) SkyMapZoom::moveDown, 0, +25));
    }

    private interface SkyMapZoomAction {

        void action(SkyMapZoom skyMapZoom);
    }
}
