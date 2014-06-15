package com.piotrglazar.nightowl.util;

import com.piotrglazar.nightowl.ui.MainWindow;

@FunctionalInterface
public interface UiUpdateAction {

    void updateUi(MainWindow mainWindow);
}
