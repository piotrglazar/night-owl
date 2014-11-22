package com.piotrglazar.nightowl.util;

import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "com.piotrglazar.nightowl:name=StarPositionCache")
public class StarPositionCacheBean {

    private final GuavaCacheManager guavaCacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public StarPositionCacheBean(GuavaCacheManager guavaCacheManager, ApplicationEventPublisher applicationEventPublisher) {
        this.guavaCacheManager = guavaCacheManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @ManagedOperation(description = "Resets star position cache and repaints user interface")
    public void clearCacheAndRepaintUi() {
        clearCache();
        repaintUi();
    }

    private void repaintUi() {
        applicationEventPublisher.publishEvent(new UiUpdateEvent(this, MainWindow::repaintUi));
    }

    private void clearCache() {
        final Cache cache = guavaCacheManager.getCache(ApplicationConfiguration.NIGHT_OWL_CACHE);
        cache.clear();
    }
}
