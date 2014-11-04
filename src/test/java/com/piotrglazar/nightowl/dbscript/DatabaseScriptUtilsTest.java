package com.piotrglazar.nightowl.dbscript;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseScriptUtilsTest {

    @Mock
    private DatabaseScriptFileLocation databaseLocation;

    @Mock
    private DatabaseScriptCreator databaseScriptCreator;

    @Mock
    private DatabaseScriptFixer databaseScriptFixer;

    @InjectMocks
    private DatabaseScriptUtils databaseScriptUtils;

    @Test
    public void shouldPrepareDatabaseScript() {
        // given
        final Path scriptPath = Paths.get(System.getProperty("java.io.tmpdir"));
        given(databaseLocation.getDatabaseScriptFileLocation()).willReturn(scriptPath);

        // when
        databaseScriptUtils.prepareDbScript();

        // then
        final InOrder inOrder = Mockito.inOrder(databaseScriptCreator, databaseScriptFixer);
        inOrder.verify(databaseScriptCreator).replaceOldScriptFileWithNewOne(scriptPath);
        inOrder.verify(databaseScriptFixer).fixScriptFile(scriptPath);
    }
}
