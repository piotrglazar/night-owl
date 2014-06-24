package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.ui.MainWindow;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainWindowTimeProviderTest {

    @Mock
    private NightOwlRuntimeConfiguration runtimeConfiguration;

    @Mock
    private TimerFactory timerFactory;

    @Mock
    private Timer timer;

    @Mock
    private Supplier<ZonedDateTime> dateTimeSupplier;

    @Mock
    private SiderealHourAngleCalculator siderealHourAngleCalculator;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private MainWindow mainWindow;

    private MainWindowTimeProvider timeProvider;

    @Test
    public void shouldPostTimeUpdateEvents() {
        // given
        given(dateTimeSupplier.get()).willReturn(ZonedDateTime.now());
        given(timerFactory.timer(anyString())).willReturn(timer);
        given(runtimeConfiguration.getUserLocation()).willReturn(UserLocation.builder().build());
        timeProvider = new MainWindowTimeProvider(timerFactory, dateTimeSupplier, siderealHourAngleCalculator, applicationEventPublisher,
                runtimeConfiguration);

        // when
        timeProvider.setupTimerTask();

        // then
        ArgumentCaptor<TimerTask> timerTask = ArgumentCaptor.forClass(TimerTask.class);
        verify(timer).scheduleAtFixedRate(timerTask.capture(), anyLong(), anyLong());

        // run timer task
        timerTask.getValue().run();
        verify(dateTimeSupplier).get();
        ArgumentCaptor<UiUpdateEvent> updateEvents = ArgumentCaptor.forClass(UiUpdateEvent.class);
        verify(applicationEventPublisher, times(2)).publishEvent(updateEvents.capture());

        // run update events
        updateEvents.getAllValues().stream().forEach(ui -> ui.action(mainWindow));
        verify(mainWindow).setTimeLabel(any(LocalDateTime.class));
        verify(mainWindow).setSiderealHourAngleLabel(any(LocalTime.class));
        verify(siderealHourAngleCalculator).siderealHourAngle(any(ZonedDateTime.class), any(Longitude.class));
    }
}
