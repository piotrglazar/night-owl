package com.piotrglazar.nightowl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@SuppressWarnings("unused")
public class TestUiUpdater implements UiUpdater {
    @Override
    public void update(final Runnable action) {
        action.run();
    }
}
