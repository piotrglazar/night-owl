package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.AbstractContextTest;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultStarInfoRepositoryContextTest extends AbstractContextTest {

    @Autowired
    private StarInfoRepository starInfoRepository;

    @Test
    public void shouldFetchAllStarsFromPreConfiguredDatabase() {
        // when
        final List<StarInfo> allStars = starInfoRepository.findAll();

        // then
        assertThat(allStars).hasSize(6);
        assertThat(allStars.get(0).getSpectralType()).isEqualTo("O");
        assertThat(allStars.get(1).getSpectralType()).isEqualTo("B");
        assertThat(allStars.get(2).getSpectralType()).isEqualTo("A");
        assertThat(allStars.get(3).getSpectralType()).isEqualTo("F");
        assertThat(allStars.get(4).getSpectralType()).isEqualTo("G");
        assertThat(allStars.get(5).getSpectralType()).isEqualTo("K");
    }
}
