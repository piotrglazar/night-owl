package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainWindowTimeEventsTest {

    @Mock
    private NightOwlRuntimeConfiguration runtimeConfiguration;

    @Mock
    private TimerFactory timerFactory;

    @Mock
    private EventScheduler eventScheduler;

    @Mock
    private Supplier<ZonedDateTime> dateTimeSupplier;

    @Mock
    private SiderealHourAngleCalculator siderealHourAngleCalculator;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private MainWindow mainWindow;

    @Captor
    private ArgumentCaptor<Runnable> timerTask;

    @Captor
    private ArgumentCaptor<UiUpdateEvent> updateEvents;

    private MainWindowTimeEvents timeProvider;

    @Before
    public void setUp() {
        timeProvider = new MainWindowTimeEvents(eventScheduler, dateTimeSupplier, siderealHourAngleCalculator,
                applicationEventPublisher, runtimeConfiguration, 1, 240);
    }

    @Test
    public void shouldRegisterPostTimeUpdateEvents() {
        // given
        given(dateTimeSupplier.get()).willReturn(ZonedDateTime.now());
        given(runtimeConfiguration.getUserLocation()).willReturn(UserLocation.builder().build());

        // when
        timeProvider.setupTimeEvents();

        // then
        verifyThatTimerTaskWasRegisteredAndRunIt("UiTimeRefresher");
        verifyThatUpdateEventsWerePublishedAndFireThem(2);

        // and then
        verify(mainWindow).setTimeLabel(any(LocalDateTime.class));
        verify(mainWindow).setSiderealHourAngleLabel(any(LocalTime.class));
        verify(siderealHourAngleCalculator).siderealHourAngle(any(ZonedDateTime.class), any(Longitude.class));
    }

    @Test
    public void shouldRegisterRepaintUiEvent() {
        // when
        timeProvider.setupTimeEvents();

        // then
        verifyThatTimerTaskWasRegisteredAndRunIt("UiSkyMapRefresher");

        verifyThatUpdateEventsWerePublishedAndFireThem(1);

        // and then
        verify(mainWindow).repaintUi();
    }

    private void verifyThatUpdateEventsWerePublishedAndFireThem(int times) {
        verify(applicationEventPublisher, times(times)).publishEvent(updateEvents.capture());

        updateEvents.getAllValues().stream().forEach(ui -> ui.action(mainWindow));
    }

    private void verifyThatTimerTaskWasRegisteredAndRunIt(String uiSkyMapRefresher) {
        verify(eventScheduler).registerAndStartCyclicEvent(eq(uiSkyMapRefresher), anyLong(), any(TimeUnit.class),
                timerTask.capture());

        timerTask.getValue().run();
    }
}
