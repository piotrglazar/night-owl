package com.piotrglazar.nightowl.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class UserLocationDtoTest {
    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(UserLocationDto.class).verify();
    }
}
