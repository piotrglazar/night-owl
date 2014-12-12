package com.piotrglazar.nightowl.ui.map;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.model.SkyDisplayContext;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.StarInfoDetails;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.time.LocalTime;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
@SuppressWarnings("unchecked")
public class SkyMapTest {

    @Mock
    private Graphics graphics;

    @Mock
    private SkyMapCircle skyMapCircle;

    @Mock
    private SkyMapDot skyMapDot;

    @Mock
    private SkyMapCalculations skyMapCalculations;

    @InjectMocks
    private SkyMap skyMap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        skyMap.setCardinalDirections(Lists.newArrayList(CardinalDirections.S));
    }

    @Test
    @Parameters({
            "true",
            "false"
    })
    public void shouldUseAllComponentsToDrawSkyMap(boolean shouldDrawStarLabel) {
        // given
        final java.util.List<CardinalDirections> cardinalDirections = mockDirectionsSigns();
        final SkyMapDto skyMapDto = new SkyMapDtoBuilder()
                                            .starPositions(Lists.newArrayList(arbitraryStarPosition()))
                                            .x(100)
                                            .y(160)
                                            .radius(65)
                                            .azimuthDistance(55.5)
                                            .skyDisplayContext(new SkyDisplayContext(0.0, shouldDrawStarLabel))
                                            .build();
        given(skyMapCalculations.starLocation(anyInt(), anyInt(), anyInt(), anyDouble(), anyDouble())).willReturn(new Point(1, 2));
        given(skyMapCalculations.distanceFromCenter(eq(160), eq(65), eq(55.5))).willReturn(23);

        // when
        skyMap.draw(graphics, skyMapDto);

        // then
        verify(skyMapCircle).draw(eq(graphics), eq(100), eq(160), eq(65));
        verify(skyMapDot).draw(eq(graphics), eq(100), eq(160));
        verify(skyMapDot).draw(eq(graphics), eq(100), eq(23));
        verify(cardinalDirections).forEach(any(Consumer.class));
        verify(graphics).fillRect(eq(199), eq(2), anyInt(), anyInt());
        // to draw or not to draw?
        verify(graphics, times(shouldDrawStarLabel ? 1 : 0)).drawString(anyString(), anyInt(), anyInt());
    }

    @Test
    @Parameters({
            "50, 75 | 100",
            "100, 75 | 50",
            "-20, 10 | 40",
            "40, 10 | -20",
            "-40, -25 | -10",
            "-10, -25 | -40"
    })
    public void shouldMirrorXCoordinateBecauseEastAndWestAreFlippedInASkyMap(int pointX, int x, int expectedFlippedX) {
        // when
        final int flippedX = skyMap.mirrorXCoordinate(pointX, x);

        // then
        assertThat(flippedX).isEqualTo(expectedFlippedX);
    }

    private StarPositionDto arbitraryStarPosition() {
        return new StarPositionDto(new StarInfo(LocalTime.of(0, 0), 0.0, "A0", new StarInfoDetails("star"), 0.0),
                new StarCelestialPosition(0.0, 0.0));
    }

    private java.util.List<CardinalDirections> mockDirectionsSigns() {
        java.util.List<CardinalDirections> cardinalDirections = mock(java.util.List.class);
        skyMap.setCardinalDirections(cardinalDirections);
        return cardinalDirections;
    }
}
