package com.piotrglazar.nightowl.util;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

@Component
public class TimeProvider implements Supplier<ZonedDateTime> {

    @Override
    public ZonedDateTime get() {
        return ZonedDateTime.now();
    }
}
