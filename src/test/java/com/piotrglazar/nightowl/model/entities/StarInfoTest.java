package com.piotrglazar.nightowl.model.entities;

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
        final StarInfo starInfo = new StarInfo(LocalTime.of(12, 35), 44.4, "A0", new StarInfoDetails("Sirius"), 12.34, StarColor.O);

        // when
        final String toString = starInfo.toString();

        // then
        assertThat(toString)
                .contains("rightAscension", Integer.toString(starInfo.getRightAscension().getHour()),
                        Integer.toString(starInfo.getRightAscension().getMinute()))
                .contains("declination", Double.toString(starInfo.getDeclination()))
                .contains("spectralType", starInfo.getSpectralType())
                .contains("starInfoDetails", starInfo.getStarInfoDetails().get().toString())
                .contains("apparentMagnitude", Double.toString(starInfo.getApparentMagnitude()))
                .contains("starColor", starInfo.getStarColor().toString());
    }
}
