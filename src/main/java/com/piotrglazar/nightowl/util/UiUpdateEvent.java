package com.piotrglazar.nightowl.util;

import com.piotrglazar.nightowl.api.MainWindow;
import org.springframework.context.ApplicationEvent;

public class UiUpdateEvent extends ApplicationEvent {

    private final UiUpdateAction uiUpdateAction;

    public UiUpdateEvent(final Object source, final UiUpdateAction uiUpdateAction) {
        super(source);
        this.uiUpdateAction = uiUpdateAction;
    }

    public void action(MainWindow mainWindow) {
        uiUpdateAction.updateUi(mainWindow);
    }
}
