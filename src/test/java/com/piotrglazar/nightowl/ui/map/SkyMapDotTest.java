package com.piotrglazar.nightowl.ui.map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Graphics;

import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class SkyMapDotTest {

    @Mock
    private Graphics graphics;

    @Mock
    private SkyMapCircle skyMapCircle;

    @InjectMocks
    private SkyMapDot skyMapDot;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters({
            "0, 0",
            "3, 8"
    })
    public void shouldRenderDot(int x, int y) {
        // when
        skyMapDot.draw(graphics, x, y);

        // then
        verify(skyMapCircle).draw(graphics, x, y, SkyMapDot.RADIUS);
    }
}
