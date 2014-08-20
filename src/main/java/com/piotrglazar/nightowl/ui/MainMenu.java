package com.piotrglazar.nightowl.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.invoke.MethodHandles;

@Component
public class MainMenu {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MainMenuController mainMenuController;

    private JMenuBar menuBar = new JMenuBar();

    @Autowired
    public MainMenu(final MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @PostConstruct
    public void prepareMenu() {
        final JMenu fileMenu = createFileMenuWithContent();
        menuBar.add(fileMenu);

        final JMenu aboutMenu = createAboutMenuWithContent();
        menuBar.add(aboutMenu);
    }

    private JMenu createFileMenuWithContent() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "The main entrypoint for actions");

        final JMenuItem locationItem = createFileMenuLocationItem();
        fileMenu.add(locationItem);

        fileMenu.addSeparator();

        final JMenuItem exitItem = createFileMenuExitItem();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenuItem createFileMenuExitItem() {
        final JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(a -> {
            int userResponse = JOptionPane.showConfirmDialog(exit, "Do you wish to quit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (userClickedOk(userResponse)) {
                closeApplication();
            }
        });
        return exit;
    }

    private void closeApplication() {
        System.exit(0);
    }

    private boolean userClickedOk(int userResponse) {
        return userResponse == JOptionPane.OK_OPTION;
    }

    private JMenuItem createFileMenuLocationItem() {
        final JMenuItem location = new JMenuItem("Location", KeyEvent.VK_T);
        location.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_MASK));
        location.getAccessibleContext().setAccessibleDescription("Change location");
        location.addActionListener(a -> {
            final Object response = JOptionPane.showInputDialog(location, "Please select your location", "Location selection",
                    JOptionPane.QUESTION_MESSAGE, null, mainMenuController.getAllUserLocations().toArray(),
                    mainMenuController.getCurrentUserLocation());
            LOG.info("User response {}", response);
            mainMenuController.updateUserLocation((com.piotrglazar.nightowl.model.UserLocationDto) response);
        });
        return location;
    }

    private JMenu createAboutMenuWithContent() {
        final JMenu aboutMenu = new JMenu("About");
        aboutMenu.getAccessibleContext().setAccessibleDescription("This menu is all about Night Owl");
        aboutMenu.setMnemonic(KeyEvent.VK_A);

        aboutMenu.add(aboutMenuContent());
        return aboutMenu;
    }

    private JMenuItem aboutMenuContent() {
        final JMenuItem menuItem = new JMenuItem("About");
        menuItem.setText("About Night Owl");
        menuItem.addActionListener(e -> JOptionPane.showMessageDialog(menuItem, "Created by Piotr Glazar", "About NightOwl",
                JOptionPane.INFORMATION_MESSAGE));

        return menuItem;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
