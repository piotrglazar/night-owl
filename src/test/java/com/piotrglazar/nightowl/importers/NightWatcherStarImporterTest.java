package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.model.StarInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NightWatcherStarImporterTest {

    @Mock
    private StarInfoProvider starInfoProvider;

    @InjectMocks
    private NightWatcherStarImporter importer;

    @Test
    public void shouldImportStars() throws IOException, URISyntaxException {
        // given
        Path path = Paths.get(getClass().getClassLoader().getResource("importers/nightwatchstars.txt").toURI());

        // when
        importer.importStars(path);

        // then
        verify(starInfoProvider, times(5)).saveStarInfo(any(StarInfo.class));
        verify(starInfoProvider).deleteAll();
    }
}
