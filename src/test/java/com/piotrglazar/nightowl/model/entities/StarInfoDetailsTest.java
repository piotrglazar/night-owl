package com.piotrglazar.nightowl.model.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StarInfoDetailsTest {

    @Test
    public void shouldMeetEqualsAndHashCodeContract() {
        // expect
        EqualsVerifier.forClass(StarInfoDetails.class).verify();
    }

    @Test
    public void shouldToStringDisplayHumanReadableMessage() {
        // given
        final StarInfoDetails starInfoDetails = new StarInfoDetails("Antares");

        // when
        final String toString = starInfoDetails.toString();

        // then
        assertThat(toString).contains("Antares");
    }
}
