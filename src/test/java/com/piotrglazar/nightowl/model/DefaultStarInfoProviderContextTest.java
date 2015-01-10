package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.AbstractContextTest;
import com.piotrglazar.nightowl.model.entities.StarColor;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.StarInfoBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultStarInfoProviderContextTest extends AbstractContextTest {

    @Autowired
    private DefaultStarInfoProvider starInfoProvider;

    @Autowired
    private StarInfoRepository starInfoRepository;

    @Test
    public void shouldReadStarsCount() {
        // when
        final long starsCount = starInfoProvider.count();

        // then
        assertThat(starsCount).isEqualTo(6);
    }

    @Test
    public void shouldFetchAllStars() {
        // given

        // when
        final List<StarInfo> allStars = starInfoProvider.getAllStars();

        // then
        assertThat(allStars).hasSize(6);
        assertThat(allStars.get(0).getSpectralType()).isEqualTo("O");
        assertThat(allStars.get(1).getSpectralType()).isEqualTo("B");
        assertThat(allStars.get(2).getSpectralType()).isEqualTo("A");
        assertThat(allStars.get(3).getSpectralType()).isEqualTo("F");
        assertThat(allStars.get(4).getSpectralType()).isEqualTo("G");
        assertThat(allStars.get(5).getSpectralType()).isEqualTo("K");
    }

    @Test
    public void shouldAddNewStar() {
        // given
        final StarInfo starInfo = new StarInfoBuilder()
                                        .rightAscension(LocalTime.of(1, 23))
                                        .declination(42.42)
                                        .spectralType("O")
                                        .name("Sirius")
                                        .apparentMagnitude(0.0)
                                        .starColor(StarColor.O)
                                        .build();

        // when
        starInfoProvider.saveStarInfo(starInfo);

        // then
        assertThat(starInfo.getId()).isNotNull();
        assertThat(starInfoRepository.findOne(starInfo.getId())).isNotNull();

        // cleanup
        starInfoRepository.delete(starInfo);
    }
}
