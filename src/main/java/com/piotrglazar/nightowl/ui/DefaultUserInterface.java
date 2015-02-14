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
import java.awt.KeyboardFocusManager;

@Component
public class DefaultUserInterface implements UserInterface, ApplicationListener<UiUpdateEvent> {

    private final MainWindow mainWindow;
    private final UiUpdater uiUpdater;
    private final MainMenu mainMenu;
    private final ArrowKeysDispatcher arrowKeysDispatcher;

    @Autowired
    public DefaultUserInterface(MainWindow mainWindow, UiUpdater uiUpdater, MainMenu mainMenu, ArrowKeysDispatcher arrowKeysDispatcher) {
        this.mainWindow = mainWindow;
        this.uiUpdater = uiUpdater;
        this.mainMenu = mainMenu;
        this.arrowKeysDispatcher = arrowKeysDispatcher;
    }

    public void runUserInterface() {
        final JFrame frame = new JFrame("NightOwl");
        frame.add(mainWindow.preDisplay().getMainPanel());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        currentKeyboardFocusManager.addKeyEventDispatcher(arrowKeysDispatcher);

        frame.setJMenuBar(mainMenu.getMenuBar());

        frame.setVisible(true);
    }

    @Override
    public void onApplicationEvent(final UiUpdateEvent event) {
        uiUpdater.update(() -> event.action(mainWindow));
    }
}
