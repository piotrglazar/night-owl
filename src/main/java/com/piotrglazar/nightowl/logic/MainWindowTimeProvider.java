package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.ui.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class MainWindowTimeProvider {

    private final MainWindow mainWindow;

    private final Timer timer;

    @Value("#{configProperties['clock.refresh.rate.in.seconds']?:1}")
    private long refresh;

    @Autowired
    public MainWindowTimeProvider(final MainWindow mainWindow, final TimerFactory timerFactory) {
        this.mainWindow = mainWindow;
        this.timer = timerFactory.timer("UiTimeRefresher");
    }

    @PostConstruct
    public void setupTimerTask() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mainWindow.setTimeLabel(LocalDateTime.now());
            }
        }, 1000L, refresh * 1000L);
    }
}
