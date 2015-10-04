package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.util.ClockEvent;
import com.piotrglazar.nightowl.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClockStatus {

    private final EventScheduler eventScheduler;
    private final TimeProvider timeProvider;

    @Autowired
    public ClockStatus(EventScheduler eventScheduler, TimeProvider timeProvider) {
        this.eventScheduler = eventScheduler;
        this.timeProvider = timeProvider;
    }

    @EventListener
    public void changeClockStatus(ClockEvent event) {
        ClockEvent.ActionType type = event.getActionType();
        if (type == ClockEvent.ActionType.START) {
            startTimeBasedActivities();
        } else if (type == ClockEvent.ActionType.STOP) {
            stopTimeBasedActivities();
        } else {
            throw new IllegalStateException("Unknown clock action type " + type);
        }
    }

    private void stopTimeBasedActivities() {
        timeProvider.cachedTime();
        eventScheduler.stopAllEvents();
    }

    private void startTimeBasedActivities() {
        timeProvider.currentTime();
        eventScheduler.startAllEvents();
    }
}
