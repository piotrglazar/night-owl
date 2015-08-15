package com.piotrglazar.nightowl.ui;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Map;

import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

@Component
public class ArrowKeysDispatcher implements KeyEventDispatcher {

    private final SkyMapZoom skyMapZoom;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<Integer, Action> actions;

    @Autowired
    public ArrowKeysDispatcher(SkyMapZoom skyMapZoom, ApplicationEventPublisher applicationEventPublisher) {
        this.skyMapZoom = skyMapZoom;
        this.applicationEventPublisher = applicationEventPublisher;
        this.actions = buildActions();
    }

    private Map<Integer, Action> buildActions() {
        return ImmutableMap.<Integer, Action>builder()
                .put(VK_UP, new SkyMapAction(skyMapZoom::moveDown))
                .put(VK_DOWN, new SkyMapAction(skyMapZoom::moveUp))
                .put(VK_LEFT, new SkyMapAction(skyMapZoom::moveRight))
                .put(VK_RIGHT, new SkyMapAction(skyMapZoom::moveLeft))
                .put(VK_DELETE, new SkyMapAction(skyMapZoom::reset))
                .build();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        return isKeyPressEvent(e) && handleEventIfArrowKeyPressed(e);
    }

    private boolean handleEventIfArrowKeyPressed(KeyEvent keyEvent) {
        final Action action = actions.getOrDefault(keyEvent.getKeyCode(), () -> false);
        return action.perform();
    }

    private boolean isKeyPressEvent(KeyEvent keyEvent) {
        return keyEvent.getID() == KEY_PRESSED;
    }

    private interface Action {
        boolean perform();
    }

    private final class SkyMapAction implements Action {

        private final Runnable action;

        private SkyMapAction(Runnable action) {
            this.action = action;
        }

        @Override
        public boolean perform() {
            action.run();
            applicationEventPublisher.publishEvent(new UiUpdateEvent(MainWindow::repaintUi));
            return true;
        }
    }
}
