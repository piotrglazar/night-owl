package com.piotrglazar.nightowl.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarInfoRepository extends JpaRepository<StarInfo, Long> {

    List<StarInfo> findByDeclinationGreaterThan(double declination);

    List<StarInfo> findByDeclinationLessThan(double declination);

    List<StarInfo> findByDeclinationBetween(double lowerBoundary, double upperBoundary);
}
