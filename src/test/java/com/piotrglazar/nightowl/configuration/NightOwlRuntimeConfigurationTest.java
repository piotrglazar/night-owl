package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.api.RuntimeConfigurationProvider;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.SkyDisplayContext;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NightOwlRuntimeConfigurationTest {

    private RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private RuntimeConfigurationProvider configurationProvider;

    @Mock
    private MainWindow mainWindow;

    @InjectMocks
    private NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;

    @Before
    public void setUp() {
        runtimeConfiguration.setVisibilitySettings(new SkyObjectVisibilitySettings());
    }

    @Test
    public void shouldPostEventWithUserConfiguration() {
        // given
        given(configurationProvider.getConfiguration()).willReturn(runtimeConfiguration);

        // when @PostConstruct
        nightOwlRuntimeConfiguration.loadRuntimeConfiguration();

        // then
        ArgumentCaptor<UiUpdateEvent> uiUpdateEvent = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher).publishEvent(uiUpdateEvent.capture());
        // fire event
        uiUpdateEvent.getValue().action(mainWindow);
        verify(mainWindow).setUserLocation(any(UserLocation.class));
    }

    @Test
    public void shouldUpdateUserLocation() throws Exception {
        // given
        final UserLocation userLocation = arbitraryUserLocation();
        given(configurationProvider.getConfiguration()).willReturn(runtimeConfiguration);
        nightOwlRuntimeConfiguration.loadRuntimeConfiguration();

        // when
        nightOwlRuntimeConfiguration.updateUserLocation(userLocation);

        // then
        assertThat(runtimeConfiguration.getChosenUserLocation()).isEqualTo(userLocation);
        verify(configurationProvider).updateConfiguration(runtimeConfiguration);
        ArgumentCaptor<UiUpdateEvent> uiUpdateEvent = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(2)).publishEvent(uiUpdateEvent.capture());
        // fire event
        assertThat(uiUpdateEvent.getAllValues()).hasSize(2);
        // ignore the first event, it's tested above
        uiUpdateEvent.getAllValues().get(1).action(mainWindow);
        verify(mainWindow).setUserLocation(any(UserLocation.class));
    }

    @Test
    public void shouldUpdateStarVisibilityMagnitude() {
        // given
        given(configurationProvider.getConfiguration()).willReturn(runtimeConfiguration);
        nightOwlRuntimeConfiguration.loadRuntimeConfiguration();
        final double starVisibilityMagnitude = 2.0;

        // when
        nightOwlRuntimeConfiguration.updateStarVisibilityMagnitude(starVisibilityMagnitude);

        // then
        assertThat(nightOwlRuntimeConfiguration.getStarVisibilityMagnitude()).isEqualTo(starVisibilityMagnitude);
        verify(configurationProvider).updateConfiguration(runtimeConfiguration);
    }

    @Test
    public void shouldCreateSkyDisplayContext() throws Exception {
        // given
        given(configurationProvider.getConfiguration()).willReturn(runtimeConfiguration);
        nightOwlRuntimeConfiguration.loadRuntimeConfiguration();
        setSkyObjectVisibilitySettings(1.0, true);

        // when
        final SkyDisplayContext skyDisplayContext = nightOwlRuntimeConfiguration.skyDisplayContext();

        // then
        assertThat(skyDisplayContext.getStarVisibilityMag()).isEqualTo(1.0);
        assertThat(skyDisplayContext.shouldShowStarLabels()).isEqualTo(true);
    }

    @Test
    public void shouldUpdateShowStarLabels() {
        // given
        setUpVisibilitySettings();
        given(configurationProvider.getConfiguration()).willReturn(runtimeConfiguration);
        nightOwlRuntimeConfiguration.loadRuntimeConfiguration();

        // when
        nightOwlRuntimeConfiguration.updateShowStarLabels(true);

        // then
        assertThat(nightOwlRuntimeConfiguration.skyDisplayContext().shouldShowStarLabels()).isTrue();
        verify(configurationProvider).updateConfiguration(runtimeConfiguration);
    }

    private void setUpVisibilitySettings() {
        final SkyObjectVisibilitySettings visibilitySettings = runtimeConfiguration.getVisibilitySettings();
        visibilitySettings.setShowStarLabels(false);
        visibilitySettings.setStarVisibilityMag(0.0);
    }

    private void setSkyObjectVisibilitySettings(double starVisibilityMagnitude, boolean showStarLabels) {
        final SkyObjectVisibilitySettings visibilitySettings = runtimeConfiguration.getVisibilitySettings();
        visibilitySettings.setStarVisibilityMag(starVisibilityMagnitude);
        visibilitySettings.setShowStarLabels(showStarLabels);
    }

    private UserLocation arbitraryUserLocation() {
        return new UserLocation(new Latitude(51.0), new Longitude(21.0), "Warsaw");
    }
}
