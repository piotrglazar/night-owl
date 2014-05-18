package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;


@Component
public class DefaultUserInterface implements UserInterface {

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
}
