package com.piotrglazar.nightowl.api;

import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.messages.StarsVisibilityMessage;

import javax.swing.JPanel;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface MainWindow {
    MainWindow preDisplay();

    JPanel getMainPanel();

    void setSiderealHourAngleLabel(LocalTime siderealHourAngle);

    void setTimeLabel(LocalDateTime now);

    void setNumberOfStars(long numberOfStars);

    void setNumberOfUserLocations(long numberOfUserLocations);

    void setNumberOfStarsVisibleNow(long numberOfStarsVisibleNow);

    void setUserLocation(UserLocation userLocation);

    void setStarsVisibility(StarsVisibilityMessage message);

    void repaintUi();
}
