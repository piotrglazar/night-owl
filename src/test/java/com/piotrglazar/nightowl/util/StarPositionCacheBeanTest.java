package com.piotrglazar.nightowl.util;

import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StarPositionCacheBeanTest {

    @Captor
    private ArgumentCaptor<UiUpdateEvent> captor;

    @Mock
    private MainWindow mainWindow;

    @Mock
    private Cache cache;

    @Mock
    private GuavaCacheManager guavaCacheManager;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private StarPositionCacheBean starPositionCacheBean;

    @Test
    public void shouldResetCacheAndReloadUi() {
        // given
        given(guavaCacheManager.getCache(ApplicationConfiguration.NIGHT_OWL_CACHE)).willReturn(cache);

        // when
        starPositionCacheBean.clearCacheAndRepaintUi();

        // then
        verify(cache).clear();
        verifyThatRepaintUiMessageWasCalled();
    }

    private void verifyThatRepaintUiMessageWasCalled() {
        verify(applicationEventPublisher).publishEvent(captor.capture());
        captor.getValue().action(mainWindow);
        verify(mainWindow).repaintUi();
    }
}
