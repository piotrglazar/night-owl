package com.piotrglazar.nightowl.model.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class UserLocationTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(UserLocation.class).verify();
    }
}
