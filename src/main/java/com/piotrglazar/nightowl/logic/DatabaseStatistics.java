package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.UserLocationProvider;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.messages.StarsVisibilityMessage;
import com.piotrglazar.nightowl.util.StateReloadEvent;
import com.piotrglazar.nightowl.util.TimeProvider;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class DatabaseStatistics implements ApplicationListener<StateReloadEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationEventPublisher applicationEventPublisher;
    private final StarPositionProvider starPositionProvider;
    private final UserLocationProvider userLocationProvider;
    private final StarInfoProvider starInfoProvider;
    private final NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;
    private final TimeProvider timeProvider;

    @Autowired
    public DatabaseStatistics(ApplicationEventPublisher applicationEventPublisher, StarPositionProvider starPositionProvider,
                              UserLocationProvider userLocationProvider, StarInfoProvider starInfoProvider,
                              NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration, TimeProvider timeProvider) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.starPositionProvider = starPositionProvider;
        this.userLocationProvider = userLocationProvider;
        this.starInfoProvider = starInfoProvider;
        this.nightOwlRuntimeConfiguration = nightOwlRuntimeConfiguration;
        this.timeProvider = timeProvider;
    }

    @PostConstruct
    public void displayDatabaseStatisticsOnUi() {
        applicationEventPublisher.publishEvent(starInfoStatistics());
        applicationEventPublisher.publishEvent(userLocationStatistics());
        applicationEventPublisher.publishEvent(starVisibilityStatistics());
        publishEventAboutStarsVisibleRightNow();
    }

    @Async
    public void publishEventAboutStarsVisibleRightNow() {
        final List<StarPositionDto> starsPositions =
                starPositionProvider.getStarPositions(nightOwlRuntimeConfiguration.getUserLocation(), timeProvider.get());
        applicationEventPublisher.publishEvent(
                new UiUpdateEvent(this, mainWindow -> mainWindow.setNumberOfStarsVisibleNow(starsPositions.size())));
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

    @Override
    public void onApplicationEvent(final StateReloadEvent event) {
        LOG.info("Reloading database statistics, cause: {}", event.getCause());
        displayDatabaseStatisticsOnUi();
    }
}
