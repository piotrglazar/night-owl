package com.piotrglazar.nightowl.util;

import com.piotrglazar.nightowl.api.MainWindow;

@FunctionalInterface
public interface UiUpdateAction {

    void updateUi(MainWindow mainWindow);
}
