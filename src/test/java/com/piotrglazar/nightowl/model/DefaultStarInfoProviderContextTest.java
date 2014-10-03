package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.AbstractContextTest;
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
        assertThat(allStars.get(0).getSpectralType()).isEqualTo("A0");
        assertThat(allStars.get(1).getSpectralType()).isEqualTo("B0");
        assertThat(allStars.get(2).getSpectralType()).isEqualTo("C0");
        assertThat(allStars.get(3).getSpectralType()).isEqualTo("D0");
        assertThat(allStars.get(4).getSpectralType()).isEqualTo("E0");
        assertThat(allStars.get(5).getSpectralType()).isEqualTo("F0");
    }

    @Test
    public void shouldAddNewStar() {
        // given
        final StarInfo starInfo = new StarInfoBuilder()
                                        .rightAscension(LocalTime.now())
                                        .declination(42.42)
                                        .spectralType("A0")
                                        .name("Sirius")
                                        .apparentMagnitude(0.0)
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
