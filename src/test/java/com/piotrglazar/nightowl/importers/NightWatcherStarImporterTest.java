package com.piotrglazar.nightowl.importers;

import com.google.common.base.Charsets;
import com.piotrglazar.nightowl.TestFileUtils;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.StarInfoDetails;
import com.piotrglazar.nightowl.util.wrappers.FileReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("all")
public class NightWatcherStarImporterTest implements TestFileUtils {

    @Captor
    private ArgumentCaptor<StarInfo> starInfoCaptor;

    @Mock
    private FileReader fileReader;

    @Mock
    private ImportedLineFixer importedLineFixer;

    @Mock
    private StarInfoRepository starInfoRepository;

    @InjectMocks
    private NightWatcherStarImporter importer;

    @Test
    public void shouldImportStars() throws IOException, URISyntaxException {
        // given
        final Path path = resourcePath("importers/nightwatchstars.txt");
        given(fileReader.getNotEmptyLines(path)).willReturn(Files.lines(path, Charsets.UTF_8));

        // when
        importer.importStars(path);

        // then
        verify(starInfoRepository, times(5)).save(any(StarInfo.class));
        verify(starInfoRepository).deleteAll();
    }

    @Test
    public void shouldRemoveInvalidStarNameAndStarInfoDetailsInStarInfoMustBeNull() {
        // given
        final Path path = tmpDirPath();
        given(fileReader.getNotEmptyLines(path)).willReturn(Stream.of("0,4 1,9 5 -1 -1 G5III 6 4 7 2 4 Psc X Sirius"));
        given(importedLineFixer.shouldBeRemoved("Sirius")).willReturn(true);

        // when
        importer.importStars(path);

        // then
        verify(starInfoRepository).save(starInfoCaptor.capture());
        assertThat(getStarInfoDetails().isPresent()).isFalse();
    }

    @Test
    public void shouldReplaceInvalidStarName() {
        // given
        final Path path = tmpDirPath();
        given(fileReader.getNotEmptyLines(path)).willReturn(Stream.of("0,4 1,9 5 -1 -1 G5III 6 4 7 2 4 Psc X Sirius"));
        given(importedLineFixer.shouldBeReplaced("Sirius")).willReturn(true);
        given(importedLineFixer.getReplacement("Sirius")).willReturn("Antares");

        // when
        importer.importStars(path);

        // then
        verify(starInfoRepository).save(starInfoCaptor.capture());
        assertThat(getStarName()).isEqualTo("Antares");
    }

    @Test
    public void shouldIgnoreStarEntriesWithInvalidShardsLength() {
        // given
        // a sample from imported file
        final String invalidData = "15,38  30,28  5883   16999  -20458        75312      0        0                               ";

        // when
        Stream<StarInfo> starInfo = importer.processStars(Stream.of(invalidData));

        // then
        assertThat(starInfo.count()).isEqualTo(0);
    }

    @Test
    public void shouldIgnoreStarEntriesWithInvalidSpectralType() {
        // given
        // a sample from imported file
        final String invalidData = " 5,089920661   1,17763581  5870     930    -162 N5     23680  32736      939 112406";

        // when
        Stream<StarInfo> starInfo = importer.processStars(Stream.of(invalidData));

        // then
        assertThat(starInfo.count()).isEqualTo(0);
    }

    private String getStarName() {
        return getStarInfoDetails().map(StarInfoDetails::getName).orElseThrow(() -> new IllegalStateException("no star name"));
    }

    private Optional<StarInfoDetails> getStarInfoDetails() {
        return starInfoCaptor.getValue().getStarInfoDetails();
    }
}
