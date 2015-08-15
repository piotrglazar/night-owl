package com.piotrglazar.nightowl.logic;

import java.util.Timer;

public class TimeMetadata {

    private final String name;
    private final long intervalInMillis;

    private final Runnable action;
    private boolean active;
    private Timer timer;

    public TimeMetadata(String name, long intervalInMillis, Runnable action, boolean active, Timer timer) {
        this.name = name;
        this.intervalInMillis = intervalInMillis;
        this.action = action;
        this.active = active;
        this.timer = timer;
    }

    public String getName() {
        return name;
    }

    public long getIntervalInMillis() {
        return intervalInMillis;
    }

    public Runnable getAction() {
        return action;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isNotActive() {
        return !active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public static TimeMetadata active(String name, long intervalInMillis, Runnable action, Timer timer) {
        return new TimeMetadata(name, intervalInMillis, action, true, timer);
    }
}
