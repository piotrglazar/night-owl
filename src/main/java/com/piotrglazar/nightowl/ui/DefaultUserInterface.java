package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.UserInterface;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.swing.*;


@Component
public class DefaultUserInterface implements UserInterface, ApplicationListener<UiUpdateEvent> {

    private final MainWindow mainWindow;

    @Autowired
    public DefaultUserInterface(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void runUserInterface() {
        final JFrame frame = new JFrame("NightOwl");
        frame.setContentPane(mainWindow.preDisplay().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onApplicationEvent(final UiUpdateEvent event) {
        event.action(mainWindow);
    }
}
