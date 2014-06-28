package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.UiUpdater;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Profile("default")
@SuppressWarnings("unused")
public class DefaultUiUpdater implements UiUpdater {
    @Override
    public void update(final Runnable action) {
        SwingUtilities.invokeLater(action);
    }
}
