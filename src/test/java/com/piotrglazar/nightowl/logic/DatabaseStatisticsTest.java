package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseStatisticsTest {

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
        given(starInfoProvider.starsCount()).willReturn(42L);

        // when
        databaseStatistics.displayDatabaseStatisticsOnUi();

        // then
        ArgumentCaptor<UiUpdateEvent> captor = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher).publishEvent(captor.capture());
        // then ui event will update ui
        captor.getValue().action(mainWindow);
        verify(mainWindow).setNumberOfStars(42L);
    }
}
