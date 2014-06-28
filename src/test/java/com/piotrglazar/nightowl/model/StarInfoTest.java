package com.piotrglazar.nightowl.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class StarInfoTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(StarInfo.class).verify();
    }
}
