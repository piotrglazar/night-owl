package com.piotrglazar.nightowl.util;

import org.springframework.context.ApplicationEvent;

public class ClockEvent extends ApplicationEvent {

    public enum ActionType {
        START, STOP
    }

    private final ActionType actionType;

    public ClockEvent(Object source, ActionType actionType) {
        super(source);
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
