package com.piotrglazar.nightowl.util;

import com.piotrglazar.nightowl.api.MainWindow;

public class UiUpdateEvent implements NightOwlEvent {

    private final UiUpdateAction uiUpdateAction;

    public UiUpdateEvent(UiUpdateAction uiUpdateAction) {
        this.uiUpdateAction = uiUpdateAction;
    }

    public void action(MainWindow mainWindow) {
        uiUpdateAction.updateUi(mainWindow);
    }
}
