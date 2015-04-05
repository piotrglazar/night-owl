package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.api.StarInfoProvider;
import com.piotrglazar.nightowl.api.UserLocationProvider;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.StarPositionDto;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.TimeProvider;
import com.piotrglazar.nightowl.util.messages.DatabaseStatisticsMessage;
import com.piotrglazar.nightowl.util.messages.StarsVisibilityMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseStatistics {

    private final StarPositionProvider starPositionProvider;
    private final UserLocationProvider userLocationProvider;
    private final StarInfoProvider starInfoProvider;
    private final NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;
    private final TimeProvider timeProvider;

    @Autowired
    public DatabaseStatistics(StarPositionProvider starPositionProvider,
                              UserLocationProvider userLocationProvider, StarInfoProvider starInfoProvider,
                              NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration, TimeProvider timeProvider) {
        this.starPositionProvider = starPositionProvider;
        this.userLocationProvider = userLocationProvider;
        this.starInfoProvider = starInfoProvider;
        this.nightOwlRuntimeConfiguration = nightOwlRuntimeConfiguration;
        this.timeProvider = timeProvider;
    }

    public DatabaseStatisticsMessage getDatabaseStatisticsMessage() {
        StarsVisibilityMessage starsVisibilityMessage = getStarsVisibilityMessage();
        UserLocation userLocation = nightOwlRuntimeConfiguration.getUserLocation();
        List<StarPositionDto> starsPositions =
                    starPositionProvider.getStarPositions(nightOwlRuntimeConfiguration.getUserLocation(), timeProvider.get());

        return new DatabaseStatisticsMessage(starInfoProvider.count(), userLocationProvider.count(),
                starsPositions.size(), starsVisibilityMessage.getStarsAlwaysVisible(),
                starsVisibilityMessage.getStarsNeverVisible(), userLocation.getName(), userLocation.getLatitude().getLatitude(),
                userLocation.getLongitude().getLongitude());
    }

    public StarsVisibilityMessage getStarsVisibilityMessage() {
        final UserLocation userLocation = nightOwlRuntimeConfiguration.getUserLocation();
        final long numberOfStarsAlwaysVisible = starPositionProvider.getNumberOfStarsAlwaysVisible(userLocation);
        final long numberOfStarsNeverVisible = starPositionProvider.getNumberOfStarsNeverVisible(userLocation);
        final long numberOfStarsSometimesVisible = starPositionProvider.getNumberOfStarsSometimesVisible(userLocation);

        return new StarsVisibilityMessage(numberOfStarsAlwaysVisible, numberOfStarsSometimesVisible, numberOfStarsNeverVisible);
    }
}
