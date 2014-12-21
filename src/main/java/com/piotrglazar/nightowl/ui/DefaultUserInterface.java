package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.api.UiUpdater;
import com.piotrglazar.nightowl.api.UserInterface;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

@Component
public class DefaultUserInterface implements UserInterface, ApplicationListener<UiUpdateEvent> {

    private final MainWindow mainWindow;
    private final UiUpdater uiUpdater;
    private final MainMenu mainMenu;

    @Autowired
    public DefaultUserInterface(final MainWindow mainWindow, final UiUpdater uiUpdater, final MainMenu mainMenu) {
        this.mainWindow = mainWindow;
        this.uiUpdater = uiUpdater;
        this.mainMenu = mainMenu;
    }

    public void runUserInterface() {
        final JFrame frame = new JFrame("NightOwl");
        frame.add(mainWindow.preDisplay().getMainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        frame.setJMenuBar(mainMenu.getMenuBar());

        frame.setVisible(true);
    }

    @Override
    public void onApplicationEvent(final UiUpdateEvent event) {
        uiUpdater.update(() -> event.action(mainWindow));
    }
}
