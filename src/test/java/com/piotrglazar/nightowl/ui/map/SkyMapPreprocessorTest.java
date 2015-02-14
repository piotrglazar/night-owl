package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.ui.SkyMapRotations;
import com.piotrglazar.nightowl.ui.SkyMapZoom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SkyMapPreprocessorTest {

    @Mock
    private Graphics2D graphics2D;

    @Mock
    private AffineTransform affineTransform;

    @Mock
    private SkyMapRotations skyMapRotations;

    @Mock
    private SkyMapZoom skyMapZoom;

    @InjectMocks
    private SkyMapPreprocessor skyMapPreprocessor;

    @Before
    public void setUp() {
        given(graphics2D.getTransform()).willReturn(affineTransform);
    }

    @Test
    public void shouldRotateSkyMap() {
        // given
        given(skyMapRotations.getRotationRadians()).willReturn(20d);

        // when
        skyMapPreprocessor.preProcess(graphics2D, new SkyMapPreprocessingContext(100, 100));

        // then
        verify(affineTransform).rotate(20d, 100, 100);
        verify(graphics2D).setTransform(affineTransform);
    }

    @Test
    public void shouldZoomSkyMap() {
        // given
        given(skyMapZoom.getScale()).willReturn(2.5);

        // when
        skyMapPreprocessor.preProcess(graphics2D, new SkyMapPreprocessingContext(100, 100));

        // then
        // the actual order of invocation is important because if we called scale before translate, map center will not remain
        // at panel center after zooming in or zooming out
        final InOrder inOrder = Mockito.inOrder(affineTransform);
        inOrder.verify(affineTransform).translate(-150, -150);
        inOrder.verify(affineTransform).scale(2.5, 2.5);
        verify(graphics2D).setTransform(affineTransform);
    }

    @Test
    public void shouldMoveSkyMap() {
        // given
        given(skyMapZoom.getShiftX()).willReturn(42);
        given(skyMapZoom.getShiftY()).willReturn(42);

        // when
        skyMapPreprocessor.preProcess(graphics2D, new SkyMapPreprocessingContext(100, 100));

        // then
        // scaling the sky map is done before moving it
        verify(affineTransform).translate(eq(100.0), eq(100.0));
        verify(affineTransform).translate(eq(42.0), eq(42.0));
        verify(graphics2D).setTransform(affineTransform);
    }
}
