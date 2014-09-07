package com.piotrglazar.nightowl.ui;

import com.google.common.base.Preconditions;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.model.UserLocationDto;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.util.StateReloadEvent;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainMenuController {

    private final ObjectFactory<UserLocationRepository> userLocationRepository;
    private final ObjectFactory<NightOwlRuntimeConfiguration> nightOwlRuntimeConfiguration;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MainMenuController(ObjectFactory<UserLocationRepository> userLocationRepository,
                              ObjectFactory<NightOwlRuntimeConfiguration> nightOwlRuntimeConfiguration,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.userLocationRepository = userLocationRepository;
        this.nightOwlRuntimeConfiguration = nightOwlRuntimeConfiguration;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public List<UserLocationDto> getAllUserLocations() {
        return userLocationRepository.getObject().findAll().stream().map(UserLocationDto::fromUserLocation).collect(Collectors.toList());
    }

    public UserLocationDto getCurrentUserLocation() {
        return UserLocationDto.fromUserLocation(nightOwlRuntimeConfiguration.getObject().getUserLocation());
    }

    public void updateUserLocation(UserLocationDto userLocationDto) {
        // cancel button was pressed, nothing to do
        if (userLocationDto == null) {
            return;
        }

        final UserLocation userLocation = userLocationRepository.getObject().findOne(userLocationDto.getId());
        Preconditions.checkState(userLocation != null, "Invalid user location: %s", userLocationDto);
        nightOwlRuntimeConfiguration.getObject().updateUserLocation(userLocation);
        applicationEventPublisher.publishEvent(new StateReloadEvent(this, "changing user location"));
    }
}
