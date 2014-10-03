package com.piotrglazar.nightowl.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MoreCollectorsTest {

    @Test
    public void shouldCollectDataToImmutableList() {
        // given
        final Integer[] ints = {1, 2, 3, 4, 5};

        // when
        final List<Integer> collectedInts = Arrays.asList(ints).stream().collect(MoreCollectors.toImmutableList());

        // then
        assertThat(collectedInts)
                .containsOnly(ints)
                .isInstanceOf(ImmutableList.class);
    }
}