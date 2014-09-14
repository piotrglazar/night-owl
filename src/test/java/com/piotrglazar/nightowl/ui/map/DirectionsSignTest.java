package com.piotrglazar.nightowl.ui.map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class DirectionsSignTest {

    @Mock
    private Graphics graphics;

    @Captor
    private ArgumentCaptor<int[]> xsCaptor;

    @Captor
    private ArgumentCaptor<int[]> ysCaptor;

    @Captor
    private ArgumentCaptor<Integer> pointCountCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        given(graphics.getFontMetrics()).willReturn(mock(FontMetrics.class));
    }

    @Test
    @Parameters({
            "4, 4, 4 | 0",
            "5, 5, 1 | 4"
    })
    public void shouldNorthRenderOnUpperPart(int x, int y, int r, int expectedY) {
        // when
        DirectionsSign.N.draw(graphics, x, y, r);

        // then
        verifyThatPolygonAndSignWereDrawn("N");
        assertThat(ysCaptor.getValue()).contains(expectedY);
    }

    @Test
    @Parameters({
            "4, 4, 4 | 8",
            "5, 5, 1 | 6"
    })
    public void shouldSouthRenderOnLowerPart(int x, int y, int r, int expectedY) {
        // when
        DirectionsSign.S.draw(graphics, x, y, r);

        // then
        verifyThatPolygonAndSignWereDrawn("S");
        assertThat(ysCaptor.getValue()).contains(expectedY);
    }

    @Test
    @Parameters({
            "4, 4, 4 | 8",
            "5, 5, 1 | 6"
    })
    public void shouldEastRenderOnRightPart(int x, int y, int r, int expectedX) {
        // when
        DirectionsSign.E.draw(graphics, x, y, r);

        // then
        verifyThatPolygonAndSignWereDrawn("E");
        assertThat(xsCaptor.getValue()).contains(expectedX);
    }

    @Test
    @Parameters({
            "4, 4, 4 | 0",
            "5, 5, 1 | 4"
    })
    public void shouldWestRenderOnRightLeft(int x, int y, int r, int expectedX) {
        // when
        DirectionsSign.W.draw(graphics, x, y, r);

        // then
        verifyThatPolygonAndSignWereDrawn("W");
        assertThat(xsCaptor.getValue()).contains(expectedX);
    }

    private void verifyThatPolygonAndSignWereDrawn(final String sign) {
        verify(graphics).fillPolygon(xsCaptor.capture(), ysCaptor.capture(), pointCountCaptor.capture());
        verify(graphics).drawString(eq(sign), anyInt(), anyInt());
        assertThat(pointCountCaptor.getValue()).isEqualTo(4);
    }
}
