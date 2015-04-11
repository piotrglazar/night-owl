package com.piotrglazar.nightowl.util;

import org.junit.Test;

import java.time.ZonedDateTime;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeProviderTest {

    private TimeProvider timeProvider = new TimeProvider();

    @Test
    public void shouldCurrentTimeStrategyProvideFreshDate() throws InterruptedException {
        // given
        timeProvider.currentTime();

        // when
        ZonedDateTime first = timeProvider.get();
        sleep(10);
        ZonedDateTime second = timeProvider.get();

        // then
        assertThat(first).isBefore(second);
    }

    @Test
    public void shouldCachedTimeStrategyProvideStaleDate() throws InterruptedException {
        // given
        timeProvider.cachedTime();

        // when
        ZonedDateTime first = timeProvider.get();
        sleep(10);
        ZonedDateTime second = timeProvider.get();


        // then
        assertThat(first).isEqualTo(second);
    }
}
