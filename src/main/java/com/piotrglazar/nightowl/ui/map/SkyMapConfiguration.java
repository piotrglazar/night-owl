package com.piotrglazar.nightowl.ui.map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SkyMapConfiguration {

    @Bean
    public List<CardinalDirections> directionsSigns() {
        return Arrays.asList(CardinalDirections.values());
    }
}
