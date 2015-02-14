package com.piotrglazar.nightowl.ui;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.KEY_RELEASED;
import static java.awt.event.KeyEvent.KEY_TYPED;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_INSERT;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;
import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(JUnitParamsRunner.class)
public class ArrowKeysDispatcherTest {

    @Mock
    private KeyEvent keyEvent;

    @Mock
    private SkyMapZoom skyMapZoom;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private ArrowKeysDispatcher arrowKeysDispatcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Parameters(method = "keyEventCodes")
    public void shouldReactToArrowAndDeleteKeys(int keyCode) {
        // given
        given(keyEvent.getID()).willReturn(KEY_PRESSED);
        given(keyEvent.getKeyCode()).willReturn(keyCode);

        // when
        final boolean consumed = arrowKeysDispatcher.dispatchKeyEvent(keyEvent);

        // then
        assertThat(consumed).isTrue();
        verify(applicationEventPublisher).publishEvent(any(ApplicationEvent.class));
    }

    @Test
    @Parameters(method = "keyEventIds")
    public void shouldNotReactToEventsOtherThanKeyPressed(int keyId) {
        // given
        given(keyEvent.getID()).willReturn(keyId);

        // when
        final boolean consumed = arrowKeysDispatcher.dispatchKeyEvent(keyEvent);

        // then
        assertThat(consumed).isFalse();
    }

    @Test
    @Parameters(method = "otherKeyEventCodes")
    public void shouldNotReactToKeysOtherThanArrowsAndDelete(int keyCode) {
        // given
        given(keyEvent.getID()).willReturn(KEY_PRESSED);
        given(keyEvent.getKeyCode()).willReturn(keyCode);

        // when
        final boolean consumed = arrowKeysDispatcher.dispatchKeyEvent(keyEvent);

        // then
        assertThat(consumed).isFalse();
        verifyZeroInteractions(applicationEventPublisher, skyMapZoom);
    }

    public Object[] keyEventCodes() {
        return $(VK_UP, VK_DOWN, VK_LEFT, VK_RIGHT, VK_DELETE);
    }

    public Object[] keyEventIds() {
        return $(KEY_RELEASED, KEY_TYPED);
    }

    public Object[] otherKeyEventCodes() {
        return $(VK_4, VK_A, VK_INSERT);
    }
}