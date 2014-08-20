package com.piotrglazar.nightowl.util;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class StarsVisibilityMessageTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(StarsVisibilityMessage.class).verify();
    }
}
