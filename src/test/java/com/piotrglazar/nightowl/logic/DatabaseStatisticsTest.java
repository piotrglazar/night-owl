package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.UserLocationProvider;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.ui.MainWindow;
import com.piotrglazar.nightowl.util.StarsVisibilityMessage;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseStatisticsTest {

    @Mock
    private NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;

    @Mock
    private StarPositionProvider starPositionProvider;

    @Mock
    private UserLocationProvider userLocationProvider;

    @Mock
    private StarInfoProvider starInfoProvider;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private MainWindow mainWindow;

    @InjectMocks
    private DatabaseStatistics databaseStatistics;

    @Test
    public void shouldSendStarCountMessage() {
        // given
        given(starInfoProvider.count()).willReturn(42L);
        given(userLocationProvider.count()).willReturn(3L);
        given(starPositionProvider.getNumberOfStarsSometimesVisible(any(UserLocation.class))).willReturn(3L);
        given(starPositionProvider.getNumberOfStarsAlwaysVisible(any(UserLocation.class))).willReturn(5L);
        given(starPositionProvider.getNumberOfStarsNeverVisible(any(UserLocation.class))).willReturn(1L);

        // when
        databaseStatistics.displayDatabaseStatisticsOnUi();

        // then
        ArgumentCaptor<UiUpdateEvent> uiUpdateEvents = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(3)).publishEvent(uiUpdateEvents.capture());
        // then ui event will update ui
        invokeUiActions(uiUpdateEvents);
        verify(mainWindow).setNumberOfStars(42);
        verify(mainWindow).setNumberOfUserLocations(3);
        verify(mainWindow).setStarsVisibility(new StarsVisibilityMessage(5, 3, 1));
    }

    private void invokeUiActions(final ArgumentCaptor<UiUpdateEvent> events) {
        for (UiUpdateEvent event : events.getAllValues()) {
            event.action(mainWindow);
        }
    }
}
