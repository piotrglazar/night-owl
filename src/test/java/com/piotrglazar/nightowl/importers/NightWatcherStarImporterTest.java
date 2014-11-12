package com.piotrglazar.nightowl.importers;

import com.google.common.base.Charsets;
import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.model.entities.StarInfo;
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
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("all")
public class NightWatcherStarImporterTest {

    @Captor
    private ArgumentCaptor<StarInfo> starInfoCaptor;

    @Mock
    private FileReader fileReader;

    @Mock
    private ImportedLineFixer importedLineFixer;

    @Mock
    private StarInfoProvider starInfoProvider;

    @InjectMocks
    private NightWatcherStarImporter importer;

    @Test
    public void shouldImportStars() throws IOException, URISyntaxException {
        // given
        final Path path = Paths.get(getClass().getClassLoader().getResource("importers/nightwatchstars.txt").toURI());
        given(fileReader.getNotEmptyLines(path)).willReturn(Files.lines(path, Charsets.UTF_8));

        // when
        importer.importStars(path);

        // then
        verify(starInfoProvider, times(5)).saveStarInfo(any(StarInfo.class));
        verify(starInfoProvider).deleteAll();
    }

    @Test
    public void shouldRemoveInvalidStarName() {
        // given
        final Path path = Paths.get(System.getProperty("java.io.tmpdir"));
        given(fileReader.getNotEmptyLines(path)).willReturn(Stream.of("0,4 1,9 5 -1 -1 G5III 6 4 7 2 4 Psc X Sirius"));
        given(importedLineFixer.shouldBeRemoved("Sirius")).willReturn(true);

        // when
        importer.importStars(path);

        // then
        verify(starInfoProvider).saveStarInfo(starInfoCaptor.capture());
        assertThat(starInfoCaptor.getValue().getName()).isEqualTo("");
    }

    @Test
    public void shouldReplaceInvalidStarName() {
        // given
        final Path path = Paths.get(System.getProperty("java.io.tmpdir"));
        given(fileReader.getNotEmptyLines(path)).willReturn(Stream.of("0,4 1,9 5 -1 -1 G5III 6 4 7 2 4 Psc X Sirius"));
        given(importedLineFixer.shouldBeReplaced("Sirius")).willReturn(true);
        given(importedLineFixer.getReplacement("Sirius")).willReturn("Antares");

        // when
        importer.importStars(path);

        // then
        verify(starInfoProvider).saveStarInfo(starInfoCaptor.capture());
        assertThat(starInfoCaptor.getValue().getName()).isEqualTo("Antares");
    }
}
