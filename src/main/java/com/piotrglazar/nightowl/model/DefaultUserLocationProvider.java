package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.api.UserLocationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class DefaultUserLocationProvider implements UserLocationProvider {

    private final UserLocationRepository userLocationRepository;

    @Autowired
    public DefaultUserLocationProvider(final UserLocationRepository userLocationRepository) {
        this.userLocationRepository = userLocationRepository;
    }

    @Override
    public long count() {
        return userLocationRepository.count();
    }
}
