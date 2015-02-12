package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.ui.SkyMapRotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SkyMapPreprocessorTest {

    @Mock
    private Graphics2D graphics2D;

    @Mock
    private AffineTransform affineTransform;

    @Mock
    private SkyMapRotations skyMapRotations;

    @InjectMocks
    private SkyMapPreprocessor skyMapPreprocessor;

    @Before
    public void setUp() {
        given(graphics2D.getTransform()).willReturn(affineTransform);
    }

    @Test
    public void shouldRotateGraphicsWithRespectToSkyMapRotations() {
        // given
        given(skyMapRotations.getRotationRadians()).willReturn(20d);

        // when
        skyMapPreprocessor.preProcess(graphics2D, new SkyMapPreprocessingContext(100, 100));

        // then
        verify(affineTransform).rotate(20d, 100, 100);
        verify(graphics2D).setTransform(affineTransform);
    }
}
