package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.model.UserLocationDto;
import com.piotrglazar.nightowl.util.messages.DatabaseStatisticsMessage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.invoke.MethodHandles;

@Component
public class MainMenu {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MainMenuController mainMenuController;
    private final MainMenuMessageFormatter formatter;

    private JMenuBar menuBar = new JMenuBar();

    @Autowired
    public MainMenu(MainMenuController mainMenuController, MainMenuMessageFormatter formatter) {
        this.mainMenuController = mainMenuController;
        this.formatter = formatter;
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
                "The main entry point for actions");

        final JMenuItem locationItem = createFileMenuLocationItem();
        fileMenu.add(locationItem);

        final JMenuItem visibilitySettingsItem = createVisibilitySettingsItem();
        fileMenu.add(visibilitySettingsItem);

        final JMenuItem skyObjectSettingsItem = createSkyObjectSettingsItem();
        fileMenu.add(skyObjectSettingsItem);

        final JCheckBoxMenuItem clockRunningItem = createClockRunningSettingsItem();
        fileMenu.add(clockRunningItem);

        fileMenu.addSeparator();

        final JMenuItem databaseStatisticsItem = createDatabaseStatisticsItem();
        fileMenu.add(databaseStatisticsItem);

        fileMenu.addSeparator();

        final JMenuItem exitItem = createFileMenuExitItem();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JCheckBoxMenuItem createClockRunningSettingsItem() {
        final JCheckBoxMenuItem clockRunning = new JCheckBoxMenuItem("Clock running");
        clockRunning.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        clockRunning.setState(true);
        clockRunning.addActionListener(a -> {
            mainMenuController.updateClockRunningStatus(clockRunning.getState());
        });
        return clockRunning;
    }

    private JMenuItem createDatabaseStatisticsItem() {
        final JMenuItem databaseStatistics = new JMenuItem("Database statistics");
        databaseStatistics.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
        databaseStatistics.addActionListener(a -> {
            DatabaseStatisticsMessage databaseStatisticsMessage = mainMenuController.databaseStatistics();
            JOptionPane.showMessageDialog(databaseStatistics, formatter.formatDatabaseStatisticsMessage(databaseStatisticsMessage),
                    "Database statistics", JOptionPane.PLAIN_MESSAGE);
        });
        return databaseStatistics;
    }

    private JMenuItem createSkyObjectSettingsItem() {
        final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Labels visibility settings");
        menuItem.setState(mainMenuController.currentShowStarLabels());
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_MASK));
        menuItem.addActionListener(a -> mainMenuController.updateStarLabelsVisibility(menuItem.isSelected()));
        return menuItem;
    }

    private JMenuItem createVisibilitySettingsItem() {
        final JMenuItem visibilitySettings = new JMenuItem("Visibility settings", KeyEvent.VK_S);
        visibilitySettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_MASK));
        visibilitySettings.addActionListener(a -> {
            final JTextField starMagnitude = starVisibilityInput();
            final int userResponse = JOptionPane.showConfirmDialog(visibilitySettings, new Object[]{"Star magnitude", starMagnitude},
                    "Update star visibility?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (userResponse == JOptionPane.YES_OPTION) {
                mainMenuController.updateStarVisibilityMagnitude(starMagnitude.getText());
            }
        });
        return visibilitySettings;
    }

    private JTextField starVisibilityInput() {
        final JTextField starMagnitude = new JTextField(Double.toString(mainMenuController.currentStarVisibilityMagnitude()));
        starMagnitude.setToolTipText(String.format("This value should be between %s and %s", mainMenuController.minimalStarMagnitude(),
                mainMenuController.maximalStarMagnitude()));
        return starMagnitude;
    }

    private JMenuItem createFileMenuExitItem() {
        final JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
        exit.addActionListener(a -> {
            int userResponse = JOptionPane.showConfirmDialog(exit, "Do you wish to quit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (userClickedOk(userResponse)) {
                closeApplication();
            }
        });
        return exit;
    }

    @SuppressWarnings("all")
    @SuppressFBWarnings
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
            mainMenuController.updateUserLocation((UserLocationDto) response);
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
