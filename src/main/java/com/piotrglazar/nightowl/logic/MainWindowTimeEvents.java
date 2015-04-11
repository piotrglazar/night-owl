package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class MainWindowTimeEvents {

    private final EventScheduler eventScheduler;
    private final Supplier<ZonedDateTime> dateTimeSupplier;
    private final SiderealHourAngleCalculator siderealHourAngleCalculator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NightOwlRuntimeConfiguration runtimeConfiguration;
    private final long refreshClock;
    private final long skyMapRefresh;

    @Autowired
    public MainWindowTimeEvents(EventScheduler eventScheduler, Supplier<ZonedDateTime> dateTimeSupplier,
                                SiderealHourAngleCalculator siderealHourAngleCalculator,
                                ApplicationEventPublisher applicationEventPublisher,
                                NightOwlRuntimeConfiguration runtimeConfiguration,
                                @Value("#{configProperties['sky.map.refresh.rate.in.seconds']?:240}") long skyMapRefresh,
                                @Value("#{configProperties['clock.refresh.rate.in.seconds']?:1}") long refreshClock) {
        this.dateTimeSupplier = dateTimeSupplier;
        this.siderealHourAngleCalculator = siderealHourAngleCalculator;
        this.runtimeConfiguration = runtimeConfiguration;
        this.eventScheduler = eventScheduler;
        this.applicationEventPublisher = applicationEventPublisher;
        this.refreshClock = refreshClock;
        this.skyMapRefresh = skyMapRefresh;
    }

    @PostConstruct
    public void setupTimeEvents() {
        eventScheduler.registerAndStartCyclicEvent("UiTimeRefresher", refreshClock, TimeUnit.SECONDS, () -> {
            final ZonedDateTime now = dateTimeSupplier.get();
            applicationEventPublisher.publishEvent(siderealHourAngleEvent(now));
            applicationEventPublisher.publishEvent(timeEvent(now));
        });
        eventScheduler.registerAndStartCyclicEvent("UiSkyMapRefresher", skyMapRefresh, TimeUnit.SECONDS, () ->
                        applicationEventPublisher.publishEvent(new UiUpdateEvent(this, MainWindow::repaintUi))
        );
    }

    private ApplicationEvent timeEvent(final ZonedDateTime now) {
        return new UiUpdateEvent(this, (mainWindow) -> mainWindow.setTimeLabel(now.toLocalDateTime()));
    }

    private ApplicationEvent siderealHourAngleEvent(final ZonedDateTime now) {
        final UserLocation userLocation = runtimeConfiguration.getUserLocation();
        LocalTime siderealHourAngle = siderealHourAngleCalculator.siderealHourAngle(now, userLocation.getLongitude());
        return new UiUpdateEvent(this, (mainWindow) -> mainWindow.setSiderealHourAngleLabel(siderealHourAngle));
    }
}
