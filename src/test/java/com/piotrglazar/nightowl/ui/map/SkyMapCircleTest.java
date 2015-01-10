package com.piotrglazar.nightowl.ui.map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.awt.Color;
import java.awt.Graphics;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class SkyMapCircleTest {

    private static final int X = 4;
    private static final int Y = 4;
    private static final int RADIUS = 10;

    @Mock
    private Graphics graphics;

    @InjectMocks
    private SkyMapCircle skyMapCircle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters({
            "4, 4, 4 | 0, 0, 8, 8",
            "8, 5, 3 | 5, 2, 6, 6"
    })
    public void shouldRenderMapBorder(int x, int y, int r, int topX, int topY, int width, int height) {
        // when
        skyMapCircle.draw(graphics, x, y, r);

        // then
        verify(graphics).drawOval(topX, topY, width, height);
    }

    @Test
    public void shouldRenderColorfulCircle() {
        // given
        final Color color = Color.BLUE;

        // when
        skyMapCircle.fillWithColor(graphics, X, Y, RADIUS, color);

        // then
        final InOrder inOrder = Mockito.inOrder(graphics);
        inOrder.verify(graphics).setColor(color);
        inOrder.verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void shouldRestoreOldColorAfterDrawingCircle() {
        // given
        final Color originalColor = Color.BLACK;
        final Color circleColor = Color.BLUE;
        given(graphics.getColor()).willReturn(originalColor);

        // when
        skyMapCircle.fillWithColor(graphics, X, Y, RADIUS, circleColor);

        // then
        final InOrder inOrder = Mockito.inOrder(graphics);
        inOrder.verify(graphics).getColor();
        inOrder.verify(graphics).setColor(circleColor);
        inOrder.verify(graphics).setColor(originalColor);
    }

    @Test
    public void shouldDrawBorderOnCircle() {
        // when
        skyMapCircle.fillWithColor(graphics, X, Y, RADIUS, Color.BLUE);

        // then
        final InOrder inOrder = Mockito.inOrder(graphics);
        inOrder.verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        inOrder.verify(graphics).drawOval(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void shouldNotDrawColorForSmallestStars() {
        // when
        skyMapCircle.fillWithColor(graphics, X, Y, 0, Color.BLUE);

        // then
        verify(graphics, never()).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
    }
}
