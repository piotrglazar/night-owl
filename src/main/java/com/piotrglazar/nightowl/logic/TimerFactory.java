package com.piotrglazar.nightowl.logic;

import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class TimerFactory {

    public Timer timer(final String name) {
        return new Timer(name);
    }
}
