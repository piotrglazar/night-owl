package com.piotrglazar.nightowl.ui.map;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.logic.StarPositionCalculator;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.model.UserLocationBuilder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.util.function.Consumer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    private NightOwlRuntimeConfiguration runtimeConfiguration;

    @Mock
    private StarPositionCalculator starPositionCalculator;

    @InjectMocks
    private SkyMap skyMap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        skyMap.setDirectionsSigns(Lists.newArrayList(DirectionsSign.S));
    }

    @Test
    @Parameters({
            "200, 350 | 100, 175, 100",
            "500, 320 | 250, 160, 160",
            "250, 250 | 125, 125, 125"
    })
    public void shouldCalculateMapRadiusAndCenterLocations(int width, int height, int expectedX, int expectedY, int expectedRadius) {
        // given
        arbitraryUserLocation();

        // when
        skyMap.draw(graphics, width, height);

        // then
        // actually any component is good, here we check whether circle x, y and r are correct
        verify(skyMapCircle).draw(graphics, expectedX, expectedY, expectedRadius);
    }

    @Test
    @Parameters({
            "45.0 | 25",
            "20.0 | 39",
            "75.0 | 8",
            "-45.0 | 75",
            "-20.0 | 61",
            "-75.0 | 92"
    })
    public void shouldCalculateSkyPoleLocation(double latitude, int expectedPole) {
        // given
        int arbitraryWidthAndHeight = 100;
        int mapCenterX = arbitraryWidthAndHeight / 2;
        userLocationWithLatitude(latitude);
        given(starPositionCalculator.poleCompletion(latitude)).willReturn(latitude);

        // when
        skyMap.draw(graphics, arbitraryWidthAndHeight, arbitraryWidthAndHeight);

        // then
        verify(skyMapDot).draw(graphics, mapCenterX, expectedPole);
    }

    @Test
    public void shouldUseAllComponentsToDrawSkyMap() {
        // given
        int arbitraryWidthAndHeight = 100;
        final java.util.List<DirectionsSign> directionsSigns = mockDirectionsSigns();
        arbitraryUserLocation();

        // when
        skyMap.draw(graphics, arbitraryWidthAndHeight, arbitraryWidthAndHeight);

        // then
        verify(skyMapCircle).draw(eq(graphics), anyInt(), anyInt(), anyInt());
        verify(skyMapDot, times(2)).draw(eq(graphics), anyInt(), anyInt());
        verify(directionsSigns).forEach(any(Consumer.class));
    }

    private java.util.List<DirectionsSign> mockDirectionsSigns() {
        java.util.List<DirectionsSign> directionsSigns = mock(java.util.List.class);
        skyMap.setDirectionsSigns(directionsSigns);
        return directionsSigns;
    }

    private void arbitraryUserLocation() {
        userLocationWithLatitude(45.0);
    }

    private void userLocationWithLatitude(double latitude) {
        UserLocation userLocation = new UserLocationBuilder().latitude(new Latitude(latitude)).build();
        given(runtimeConfiguration.getUserLocation()).willReturn(userLocation);
    }
}
