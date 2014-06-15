package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserInterfaceTest {

    @Mock
    private MainWindow mainWindow;

    @InjectMocks
    private DefaultUserInterface defaultUserInterface;

    @Test
    public void shouldConsumeUiUpdateEvents() {
        // given
        UiUpdateEvent uiUpdateEvent = new UiUpdateEvent(this, mainWindow -> mainWindow.setNumberOfStars(42));

        // when
        defaultUserInterface.onApplicationEvent(uiUpdateEvent);

        // then
        verify(mainWindow).setNumberOfStars(42);
    }
}
