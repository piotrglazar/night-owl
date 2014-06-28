package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.UiUpdater;
import com.piotrglazar.nightowl.UserInterface;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class DefaultUserInterface implements UserInterface, ApplicationListener<UiUpdateEvent> {

    private final MainWindow mainWindow;
    private final UiUpdater uiUpdater;

    @Autowired
    public DefaultUserInterface(final MainWindow mainWindow, final UiUpdater uiUpdater) {
        this.mainWindow = mainWindow;
        this.uiUpdater = uiUpdater;
    }

    public void runUserInterface() {
        final JFrame frame = new JFrame("NightOwl");
        frame.setContentPane(mainWindow.preDisplay().getMainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onApplicationEvent(final UiUpdateEvent event) {
        uiUpdater.update(() -> event.action(mainWindow));
    }
}
