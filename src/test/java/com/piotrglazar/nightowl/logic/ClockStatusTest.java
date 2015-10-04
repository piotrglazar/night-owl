package com.piotrglazar.nightowl.logic;


import com.piotrglazar.nightowl.util.ClockEvent;
import com.piotrglazar.nightowl.util.TimeProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClockStatusTest {

    @Mock
    private EventScheduler eventScheduler;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private ClockStatus clockStatus;

    @Test
    public void shouldStartClockAndTimeEvents() {
        // given
        ClockEvent clockEvent = new ClockEvent(this, ClockEvent.ActionType.START);

        // when
        clockStatus.changeClockStatus(clockEvent);

        // then
        verify(eventScheduler).startAllEvents();
        verify(timeProvider).currentTime();
    }

    @Test
    public void shouldStopClockAndTimeEvents() {
        // given
        ClockEvent clockEvent = new ClockEvent(this, ClockEvent.ActionType.STOP);

        // when
        clockStatus.changeClockStatus(clockEvent);

        // then
        verify(eventScheduler).stopAllEvents();
        verify(timeProvider).cachedTime();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenUnknownClockStatusProvided() {
        // expected
        clockStatus.changeClockStatus(new ClockEvent(this, null));
    }
}
