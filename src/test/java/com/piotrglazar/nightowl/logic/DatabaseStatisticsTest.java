package com.piotrglazar.nightowl.logic;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.api.StarInfoProvider;
import com.piotrglazar.nightowl.api.UserLocationProvider;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.util.messages.StarsVisibilityMessage;
import com.piotrglazar.nightowl.util.StateReloadEvent;
import com.piotrglazar.nightowl.util.TimeProvider;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.time.ZonedDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseStatisticsTest {

    @Mock
    private TimeProvider timeProvider;

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
        given(starPositionProvider.getStarPositions(any(UserLocation.class), any(ZonedDateTime.class))).willReturn(Lists.newLinkedList());

        // when
        databaseStatistics.displayDatabaseStatisticsOnUi();

        // then
        ArgumentCaptor<UiUpdateEvent> uiUpdateEvents = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(4)).publishEvent(uiUpdateEvents.capture());
        // then ui event will update ui
        invokeUiActions(uiUpdateEvents);
        verify(mainWindow).setNumberOfStars(42);
        verify(mainWindow).setNumberOfUserLocations(3);
        verify(mainWindow).setStarsVisibility(new StarsVisibilityMessage(5, 3, 1));
        verify(mainWindow).setNumberOfStarsVisibleNow(0);
    }

    @Test
    public void shouldRecalculateDatabaseStatisticsOnReloadStateEvent() {
        // given
        given(starInfoProvider.count()).willReturn(42L);
        given(userLocationProvider.count()).willReturn(3L);
        given(starPositionProvider.getNumberOfStarsSometimesVisible(any(UserLocation.class))).willReturn(3L);
        given(starPositionProvider.getNumberOfStarsAlwaysVisible(any(UserLocation.class))).willReturn(5L);
        given(starPositionProvider.getNumberOfStarsNeverVisible(any(UserLocation.class))).willReturn(1L);
        given(starPositionProvider.getStarPositions(any(UserLocation.class), any(ZonedDateTime.class))).willReturn(Lists.newLinkedList());

        // when
        databaseStatistics.onApplicationEvent(mock(StateReloadEvent.class));

        // then
        ArgumentCaptor<UiUpdateEvent> uiUpdateEvents = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(4)).publishEvent(uiUpdateEvents.capture());
        // then ui event will update ui
        invokeUiActions(uiUpdateEvents);
        verify(mainWindow).setNumberOfStars(42);
        verify(mainWindow).setNumberOfUserLocations(3);
        verify(mainWindow).setStarsVisibility(new StarsVisibilityMessage(5, 3, 1));
        verify(mainWindow).setNumberOfStarsVisibleNow(0);
    }

    private void invokeUiActions(final ArgumentCaptor<UiUpdateEvent> events) {
        for (UiUpdateEvent event : events.getAllValues()) {
            event.action(mainWindow);
        }
    }
}
