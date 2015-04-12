package com.piotrglazar.nightowl.logic;

import com.google.common.base.Throwables;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventSchedulerContextTest {

    private TimerFactory timerFactory = new TimerFactory();

    private EventScheduler eventScheduler = new EventScheduler(timerFactory);

    @Test(timeout = 1000)
    public void shouldUseDifferentTimersForEachTask() throws InterruptedException {
        // given
        CountDownLatch blockFirstTaskLatch = new CountDownLatch(1);
        CountDownLatch proceedToNextEvent = new CountDownLatch(1);
        CountDownLatch waitForSecondEvent = new CountDownLatch(1);

        eventScheduler.registerAndStartCyclicEvent("blocked", 100, TimeUnit.MILLISECONDS,
                new ExceptionAwareRunnable(() -> {
                    proceedToNextEvent.countDown();
                    blockFirstTaskLatch.await();
                    return null;
                }));
        proceedToNextEvent.await();

        // when
        eventScheduler.registerAndStartCyclicEvent("this must not be blocked", 100, TimeUnit.MILLISECONDS,
                new ExceptionAwareRunnable(() -> {
                    waitForSecondEvent.countDown();
                    return null;
                }));

        // then
        waitForSecondEvent.await();

        // cleanup
        blockFirstTaskLatch.countDown();
    }

    public static class ExceptionAwareRunnable implements Runnable {

        private final Callable<Void> action;

        public ExceptionAwareRunnable(Callable<Void> action) {
            this.action = action;
        }

        @Override
        public void run() {
            try {
                action.call();
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }
}
