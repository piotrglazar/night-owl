package com.piotrglazar.nightowl.logic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventSchedulerTest {

    @Mock
    private Timer timer;

    @Mock
    private TimerFactory timerFactory;

    @InjectMocks
    private EventScheduler eventScheduler;

    @Before
    public void setUp() {
        given(timerFactory.timer("test")).willReturn(timer);
    }

    @Test
    public void shouldRegisterAndStartEvent() {
        // when
        eventScheduler.registerAndStartCyclicEvent("test", 1, TimeUnit.SECONDS, () -> { });

        // then
        verify(timer).scheduleAtFixedRate(any(TimerTask.class), anyLong(), anyLong());
    }

    @Test
    public void shouldStopAllEvents() {
        // given
        eventScheduler.registerAndStartCyclicEvent("test", 1, TimeUnit.SECONDS, () -> { });

        // when
        eventScheduler.stopAllEvents();

        // then
        verify(timer).cancel();
    }

    @Test
    public void shouldStartAllEventsAfterTheyHaveBeenStopped() {
        // given
        eventScheduler.registerAndStartCyclicEvent("test", 1, TimeUnit.SECONDS, () -> { });
        eventScheduler.stopAllEvents();

        // when
        eventScheduler.startAllEvents();

        // then
        // first invocation happens after registering
        // second invocation happens on restarting
        verify(timerFactory, times(2)).timer("test");
    }

    @Test
    public void shouldNotStartAgainEventsWhichHaveNotBeenStopped() {
        // given
        eventScheduler.registerAndStartCyclicEvent("test", 1, TimeUnit.SECONDS, () -> { });

        // when
        eventScheduler.startAllEvents();

        // then
        // the only invocation happens after registering
        verify(timerFactory).timer("test");
    }
}
