package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.StarInfoProvider;
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

    @Autowired
    public DatabaseStatistics(final ApplicationEventPublisher applicationEventPublisher, final StarInfoProvider starInfoProvider) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.starInfoProvider = starInfoProvider;
    }

    @PostConstruct
    public void displayDatabaseStatisticsOnUi() {
        applicationEventPublisher.publishEvent(starInfoStatistics());
    }

    private ApplicationEvent starInfoStatistics() {
        return new UiUpdateEvent(this, (mainWindow) -> mainWindow.setNumberOfStars(starInfoProvider.starsCount()));
    }
}
