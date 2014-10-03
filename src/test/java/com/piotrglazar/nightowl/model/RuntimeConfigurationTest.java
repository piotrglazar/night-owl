package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class RuntimeConfigurationTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(RuntimeConfiguration.class).verify();
    }
}
