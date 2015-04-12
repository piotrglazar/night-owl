package com.piotrglazar.nightowl.logic;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class EventScheduler {

    private final TimerFactory timerFactory;
    private final Map<String, TimeMetadata> events;

    @Autowired
    public EventScheduler(TimerFactory timerFactory) {
        this.timerFactory = timerFactory;
        this.events = Maps.newHashMap();
    }

    public void registerAndStartCyclicEvent(String name, long units, TimeUnit timeUnit, Runnable action) {
        long intervalInMillis = convertToMilliseconds(units, timeUnit);
        Timer timer = timerFactory.timer(name);

        TimeMetadata timeMetadata = new TimeMetadata(name, intervalInMillis, action, true, timer);
        events.put(name, timeMetadata);

        timer.scheduleAtFixedRate(actionAsTimerTask(action), intervalInMillis, intervalInMillis);
    }

    private TimerTask actionAsTimerTask(Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private long convertToMilliseconds(long units, TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(units, timeUnit);
    }

    public void stopAllEvents() {
        events.values().stream()
                .filter(TimeMetadata::isActive)
                .forEach(timeMetadata -> {
                    timeMetadata.getTimer().cancel();
                    timeMetadata.setActive(false);
                });
    }

    public void startAllEvents() {
        events.values().stream()
                .filter(timeMetadata -> !timeMetadata.isActive())
                .forEach(timeMetadata -> {
                    Timer timer = timerFactory.timer(timeMetadata.getName());
                    long intervalInMillis = timeMetadata.getIntervalInMillis();
                    timer.scheduleAtFixedRate(actionAsTimerTask(timeMetadata.getAction()), intervalInMillis,
                            intervalInMillis);
                    timeMetadata.setTimer(timer);
                    timeMetadata.setActive(true);
                });
    }
}
