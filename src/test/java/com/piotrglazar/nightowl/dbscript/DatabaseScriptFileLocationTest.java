package com.piotrglazar.nightowl.dbscript;

import com.piotrglazar.nightowl.TestFileUtils;
import com.piotrglazar.nightowl.configuration.DatabaseLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseScriptFileLocationTest implements TestFileUtils {

    @Mock
    private DatabaseLocation databaseLocation;

    @InjectMocks
    private DatabaseScriptFileLocation databaseScriptFileLocation;

    @Test
    public void shouldConstructPathToScriptFile() {
        // given
        given(databaseLocation.getDatabaseRootLocation()).willReturn(tmpDir());

        // when
        final Path scriptFileLocation = databaseScriptFileLocation.getDatabaseScriptFileLocation();

        // then
        assertThat(scriptFileLocation.toString())
                .startsWith(tmpDir())
                .endsWith("database.sql");
    }
}
