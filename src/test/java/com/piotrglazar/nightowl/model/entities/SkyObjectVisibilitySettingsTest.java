package com.piotrglazar.nightowl.model.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class SkyObjectVisibilitySettingsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(SkyObjectVisibilitySettings.class).verify();
    }
}
