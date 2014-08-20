package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.RuntimeConfigurationProvider;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.ui.MainWindow;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
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
        verify(configurationProvider).updateConfiguration(runtimeConfiguration);ArgumentCaptor<UiUpdateEvent> uiUpdateEvent = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(2)).publishEvent(uiUpdateEvent.capture());
        // fire event
        assertThat(uiUpdateEvent.getAllValues()).hasSize(2);
        // ignore the first event, it's tested above
        uiUpdateEvent.getAllValues().get(1).action(mainWindow);
        verify(mainWindow).setUserLocation(any(UserLocation.class));
    }

    private UserLocation arbitraryUserLocation() {
        return new UserLocation(new Latitude(51.0), new Longitude(21.0), "Warsaw");
    }
}
