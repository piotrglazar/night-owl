package com.piotrglazar.nightowl.util;

import org.springframework.context.ApplicationEvent;

public class StateReloadEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1;

    private final String cause;

    public StateReloadEvent(Object source, String cause) {
        super(source);
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
}