package com.piotrglazar.nightowl.model.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class RuntimeConfigurationTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(RuntimeConfiguration.class).verify();
    }
}
