package com.piotrglazar.nightowl.ui.map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;

import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class SkyMapCircleTest {

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
}
