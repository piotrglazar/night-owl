package com.piotrglazar.nightowl.util;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

@Component
public class TimeProvider implements Supplier<ZonedDateTime> {

    private Supplier<ZonedDateTime> timeProvidingStrategy = ZonedDateTime::now;

    public void currentTime() {
        timeProvidingStrategy = ZonedDateTime::now;
    }

    public void cachedTime() {
        timeProvidingStrategy = new Supplier<ZonedDateTime>() {

            private final ZonedDateTime cachedTime = ZonedDateTime.now();

            @Override
            public ZonedDateTime get() {
                return cachedTime;
            }
        };
    }

    @Override
    public ZonedDateTime get() {
        return timeProvidingStrategy.get();
    }
}
