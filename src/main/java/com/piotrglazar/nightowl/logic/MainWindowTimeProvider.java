package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

@Component
public class MainWindowTimeProvider {

    @Value("#{configProperties['clock.refresh.rate.in.seconds']?:1}")
    private long refresh;
    private final Timer timer;
    private final Supplier<ZonedDateTime> dateTimeSupplier;
    private final SiderealHourAngleCalculator siderealHourAngleCalculator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NightOwlRuntimeConfiguration runtimeConfiguration;

    @Autowired
    public MainWindowTimeProvider(TimerFactory timerFactory, Supplier<ZonedDateTime> dateTimeSupplier,
                                  SiderealHourAngleCalculator siderealHourAngleCalculator,
                                  ApplicationEventPublisher applicationEventPublisher,
                                  NightOwlRuntimeConfiguration runtimeConfiguration) {
        this.dateTimeSupplier = dateTimeSupplier;
        this.siderealHourAngleCalculator = siderealHourAngleCalculator;
        this.runtimeConfiguration = runtimeConfiguration;
        this.timer = timerFactory.timer("UiTimeRefresher");
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void setupTimerTask() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final ZonedDateTime now = dateTimeSupplier.get();
                applicationEventPublisher.publishEvent(siderealHourAngleEvent(now));
                applicationEventPublisher.publishEvent(timeEvent(now));
            }
        }, 1000L, refresh * 1000L);
    }

    private ApplicationEvent timeEvent(final ZonedDateTime now) {
        return new UiUpdateEvent(this, (mainWindow) -> mainWindow.setTimeLabel(now.toLocalDateTime()));
    }

    private ApplicationEvent siderealHourAngleEvent(final ZonedDateTime now) {
        final UserLocation userLocation = runtimeConfiguration.getUserLocation();
        return new UiUpdateEvent(this,
                (mainWindow) -> mainWindow.setSiderealHourAngleLabel(siderealHourAngleCalculator.siderealHourAngle(now,
                        userLocation.getLongitude())));
    }
}
