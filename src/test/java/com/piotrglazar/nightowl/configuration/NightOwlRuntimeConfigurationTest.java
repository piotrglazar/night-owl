package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.RuntimeConfigurationProvider;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NightOwlRuntimeConfigurationTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private RuntimeConfigurationProvider configurationProvider;

    @Mock
    private RuntimeConfiguration runtimeConfiguration;

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
}
