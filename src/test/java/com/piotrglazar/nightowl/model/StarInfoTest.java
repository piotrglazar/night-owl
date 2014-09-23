package com.piotrglazar.nightowl.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class StarInfoTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(StarInfo.class).verify();
    }

    @Test
    public void shouldToStringDisplayHumanReadableMessage() {
        // given
        final StarInfo starInfo = new StarInfo(LocalTime.of(12, 35), 44.4, "A0", "Sirius", 12.34);

        // when
        final String toString = starInfo.toString();

        // then
        assertThat(toString).contains("rightAscension", "declination", "spectralType", Integer.toString(12), Integer.toString(35),
                Double.toString(44.4), "A0", "Sirius", Double.toString(12.34));
    }
}
