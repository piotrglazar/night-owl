package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
}
