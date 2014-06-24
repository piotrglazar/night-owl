package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.UserLocationProvider;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseStatistics {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StarInfoProvider starInfoProvider;
    private final UserLocationProvider userLocationProvider;

    @Autowired
    public DatabaseStatistics(ApplicationEventPublisher applicationEventPublisher, StarInfoProvider starInfoProvider,
                              UserLocationProvider userLocationProvider) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.starInfoProvider = starInfoProvider;
        this.userLocationProvider = userLocationProvider;
    }

    @PostConstruct
    public void displayDatabaseStatisticsOnUi() {
        applicationEventPublisher.publishEvent(starInfoStatistics());
        applicationEventPublisher.publishEvent(userLocationStatistics());
    }

    private ApplicationEvent userLocationStatistics() {
        return new UiUpdateEvent(this, mainWindow -> mainWindow.setNumberOfUserLocations(userLocationProvider.count()));
    }

    private ApplicationEvent starInfoStatistics() {
        return new UiUpdateEvent(this, mainWindow -> mainWindow.setNumberOfStars(starInfoProvider.count()));
    }
}
