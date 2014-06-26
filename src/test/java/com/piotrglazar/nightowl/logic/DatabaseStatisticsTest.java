package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.UserLocationProvider;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseStatisticsTest {

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

        // when
        databaseStatistics.displayDatabaseStatisticsOnUi();

        // then
        ArgumentCaptor<UiUpdateEvent> uiUpdateEvents = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(2)).publishEvent(uiUpdateEvents.capture());
        // then ui event will update ui
        invokeUiActions(uiUpdateEvents);
        verify(mainWindow).setNumberOfStars(42);
        verify(mainWindow).setNumberOfUserLocations(3);
    }

    private void invokeUiActions(final ArgumentCaptor<UiUpdateEvent> events) {
        for (UiUpdateEvent event : events.getAllValues()) {
            event.action(mainWindow);
        }
    }
}
