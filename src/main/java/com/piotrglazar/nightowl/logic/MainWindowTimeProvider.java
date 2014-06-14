package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.configuration.Localisation;
import com.piotrglazar.nightowl.ui.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

@Component
public class MainWindowTimeProvider {

    private final MainWindow mainWindow;

    private final Timer timer;

    private final Supplier<ZonedDateTime> dateTimeSupplier;

    private final SiderealHourAngleCalculator siderealHourAngleCalculator;

    @Value("#{configProperties['clock.refresh.rate.in.seconds']?:1}")
    private long refresh;

    @Autowired
    public MainWindowTimeProvider(MainWindow mainWindow, TimerFactory timerFactory, Supplier<ZonedDateTime> dateTimeSupplier,
                                  SiderealHourAngleCalculator siderealHourAngleCalculator) {
        this.mainWindow = mainWindow;
        this.dateTimeSupplier = dateTimeSupplier;
        this.siderealHourAngleCalculator = siderealHourAngleCalculator;
        this.timer = timerFactory.timer("UiTimeRefresher");
    }

    @PostConstruct
    public void setupTimerTask() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final ZonedDateTime now = dateTimeSupplier.get();
                mainWindow.setSiderealHourAngleLabel(siderealHourAngleCalculator.siderealHourAngle(now, Localisation.WARSAW_LONGITUDE));
                mainWindow.setTimeLabel(now.toLocalDateTime());
            }
        }, 1000L, refresh * 1000L);
    }
}
