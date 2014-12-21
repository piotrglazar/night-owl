package com.piotrglazar.nightowl.ui.map;

import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.logic.StarPositionProvider;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.model.entities.UserLocationBuilder;
import com.piotrglazar.nightowl.util.TimeProvider;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Graphics;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class DefaultSkyMapControllerTest {

    @Mock
    private Graphics graphics;

    @Mock
    private NightOwlRuntimeConfiguration runtimeConfiguration;

    @Mock
    private SkyMap skyMap;

    @Mock
    private StarPositionProvider starPositionProvider;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private DefaultSkyMapController skyMapController;

    @Captor
    private ArgumentCaptor<SkyMapDto> skyMapDtoCaptor;

    @Captor
    private ArgumentCaptor<Graphics> graphicsCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
        skyMapController.draw(graphics, width, height);

        // then
        verifyRuntimeConfigurationWasUsed();
        verify(skyMap).draw(graphicsCaptor.capture(), skyMapDtoCaptor.capture());
        SkyMapDto skyMapDto = skyMapDtoCaptor.getValue();
        assertThat(skyMapDto.getRadius()).isEqualTo(expectedRadius);
        assertThat(skyMapDto.getX()).isEqualTo(expectedX);
        assertThat(skyMapDto.getY()).isEqualTo(expectedY);
    }

    @Test
    public void shouldUseCachedStarPositions() {
        // given
        arbitraryUserLocation();

        // when
        skyMapController.draw(graphics, 100, 100);

        // then
        verify(starPositionProvider).getBrightStarPositionsCached(any(UserLocation.class), any(ZonedDateTime.class), anyDouble());
    }

    private void verifyRuntimeConfigurationWasUsed() {
        verify(runtimeConfiguration).skyDisplayContext();
        verify(runtimeConfiguration, atLeastOnce()).getUserLocation();
        verify(runtimeConfiguration).getStarVisibilityMagnitude();
    }

    private void arbitraryUserLocation() {
        userLocationWithLatitude(45.0);
    }

    private void userLocationWithLatitude(double latitude) {
        UserLocation userLocation = new UserLocationBuilder().latitude(new Latitude(latitude)).build();
        given(runtimeConfiguration.getUserLocation()).willReturn(userLocation);
    }
}
