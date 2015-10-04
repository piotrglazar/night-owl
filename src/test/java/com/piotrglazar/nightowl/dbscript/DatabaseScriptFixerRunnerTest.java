package com.piotrglazar.nightowl.dbscript;

import com.piotrglazar.nightowl.TestFileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseScriptFixerRunnerTest implements TestFileUtils {

    @Mock
    private DatabaseScriptFileLocation databaseLocation;

    @Mock
    private DatabaseScriptCreator databaseScriptCreator;

    @Mock
    private DatabaseScriptFixer databaseScriptFixer;

    @InjectMocks
    private DatabaseScriptFixerRunner databaseScriptFixerRunner;

    @Test
    public void shouldPrepareDatabaseScript() {
        // given
        final Path scriptPath = tmpDirPath();
        given(databaseLocation.getDatabaseScriptFileLocation()).willReturn(scriptPath);

        // when
        databaseScriptFixerRunner.prepareDbScript();

        // then
        final InOrder inOrder = Mockito.inOrder(databaseScriptCreator, databaseScriptFixer);
        inOrder.verify(databaseScriptCreator).replaceOldScriptFileWithNewOne(scriptPath);
        inOrder.verify(databaseScriptFixer).fixScriptFile(scriptPath);
    }
}
