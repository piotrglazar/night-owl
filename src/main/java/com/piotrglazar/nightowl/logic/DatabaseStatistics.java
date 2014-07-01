package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.UserLocationProvider;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.util.StarsVisibilityMessage;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseStatistics {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StarPositionProvider starPositionProvider;
    private final UserLocationProvider userLocationProvider;
    private final StarInfoProvider starInfoProvider;
    private final NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;

    @Autowired
    public DatabaseStatistics(ApplicationEventPublisher applicationEventPublisher, StarPositionProvider starPositionProvider,
                              UserLocationProvider userLocationProvider, StarInfoProvider starInfoProvider,
                              NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.starPositionProvider = starPositionProvider;
        this.userLocationProvider = userLocationProvider;
        this.starInfoProvider = starInfoProvider;
        this.nightOwlRuntimeConfiguration = nightOwlRuntimeConfiguration;
    }

    @PostConstruct
    public void displayDatabaseStatisticsOnUi() {
        applicationEventPublisher.publishEvent(starInfoStatistics());
        applicationEventPublisher.publishEvent(userLocationStatistics());
        applicationEventPublisher.publishEvent(starVisibilityStatistics());
    }

    private ApplicationEvent starVisibilityStatistics() {
        final UserLocation userLocation = nightOwlRuntimeConfiguration.getUserLocation();
        final long numberOfStarsAlwaysVisible = starPositionProvider.getNumberOfStarsAlwaysVisible(userLocation);
        final long numberOfStarsNeverVisible = starPositionProvider.getNumberOfStarsNeverVisible(userLocation);
        final long numberOfStarsSometimesVisible = starPositionProvider.getNumberOfStarsSometimesVisible(userLocation);

        final StarsVisibilityMessage message =
                new StarsVisibilityMessage(numberOfStarsAlwaysVisible, numberOfStarsSometimesVisible, numberOfStarsNeverVisible);

        return new UiUpdateEvent(this, mainWindow -> mainWindow.setStarsVisibility(message));
    }

    private ApplicationEvent userLocationStatistics() {
        return new UiUpdateEvent(this, mainWindow -> mainWindow.setNumberOfUserLocations(userLocationProvider.count()));
    }

    private ApplicationEvent starInfoStatistics() {
        return new UiUpdateEvent(this, mainWindow -> mainWindow.setNumberOfStars(starInfoProvider.count()));
    }
}
