package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.configuration.Localisation;
import com.piotrglazar.nightowl.ui.MainWindow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainWindowTimeProviderTest {

    @Mock
    private MainWindow mainWindow;

    @Mock
    private TimerFactory timerFactory;

    @Mock
    private Timer timer;

    @Mock
    private Supplier<ZonedDateTime> dateTimeSupplier;

    @Mock
    private SiderealHourAngleCalculator siderealHourAngleCalculator;

    private MainWindowTimeProvider timeProvider;

    @Test
    public void shouldRefreshTimeOnMainWindow() {
        // given
        given(dateTimeSupplier.get()).willReturn(ZonedDateTime.now());
        given(timerFactory.timer(anyString())).willReturn(timer);
        timeProvider = new MainWindowTimeProvider(mainWindow, timerFactory, dateTimeSupplier, siderealHourAngleCalculator);

        // when
        timeProvider.setupTimerTask();

        // then
        ArgumentCaptor<TimerTask> timerTask = ArgumentCaptor.forClass(TimerTask.class);
        verify(timer).scheduleAtFixedRate(timerTask.capture(), anyLong(), anyLong());
        timerTask.getValue().run();
        verify(dateTimeSupplier).get();
        verify(mainWindow).setTimeLabel(any(LocalDateTime.class));
        verify(mainWindow).setSiderealHourAngleLabel(any(LocalTime.class));
        verify(siderealHourAngleCalculator).siderealHourAngle(any(ZonedDateTime.class), eq(Localisation.WARSAW_LONGITUDE));
    }
}
