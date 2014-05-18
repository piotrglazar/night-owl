package com.piotrglazar.nightowl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NightOwlTest {

    @Mock
    private UserInterface userInterface;

    @InjectMocks
    private NightOwl nightOwl;

    @Test
    public void shouldRunUserInterface() {
        // given

        // when
        nightOwl.run();

        // then
        verify(userInterface).runUserInterface();
    }
}
