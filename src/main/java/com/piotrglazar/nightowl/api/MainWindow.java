package com.piotrglazar.nightowl.api;

import com.piotrglazar.nightowl.model.entities.UserLocation;

import javax.swing.JPanel;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface MainWindow {
    MainWindow preDisplay();

    JPanel getMainPanel();

    void setSiderealHourAngleLabel(LocalTime siderealHourAngle);

    void setTimeLabel(LocalDateTime now);

    void setUserLocation(UserLocation userLocation);

    void repaintUi();
}
