package com.piotrglazar.nightowl.model;

import com.piotrglazar.nightowl.AbstractContextTest;
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
        assertThat(starsCount).isEqualTo(2);
    }

    @Test
    public void shouldFetchAllStars() {
        // given

        // when
        final List<StarInfo> allStars = starInfoProvider.getAllStars();

        // then
        assertThat(allStars).hasSize(2);
        assertThat(allStars.get(0).getSpectralType()).isEqualTo("A0");
        assertThat(allStars.get(1).getSpectralType()).isEqualTo("B0");
    }

    @Test
    public void shouldAddNewStar() {
        // given
        final StarInfo starInfo = new StarInfo(LocalTime.now(), 42.42, "A0");

        // when
        starInfoProvider.saveStarInfo(starInfo);

        // then
        assertThat(starInfo.getId()).isNotNull();
        assertThat(starInfoRepository.findOne(starInfo.getId())).isNotNull();

        // cleanup
        starInfoRepository.delete(starInfo);
    }
}
