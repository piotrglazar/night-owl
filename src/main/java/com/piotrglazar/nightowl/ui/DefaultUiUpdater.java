package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.api.UiUpdater;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.awt.EventQueue;

@Component
@Profile("default")
@SuppressWarnings("unused")
public class DefaultUiUpdater implements UiUpdater {

    @Override
    public void update(final Runnable action) {
        EventQueue.invokeLater(action);
    }
}
