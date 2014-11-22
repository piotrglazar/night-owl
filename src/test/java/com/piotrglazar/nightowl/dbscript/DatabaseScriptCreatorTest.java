package com.piotrglazar.nightowl.dbscript;

import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseScriptCreatorTest {

    @Mock
    private Statement statement;

    @Mock
    private Connection connection;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private DatabaseScriptCreator databaseScriptCreator;

    @Before
    public void setUp() throws Exception {
        given(dataSource.getConnection()).willReturn(connection);
        given(connection.createStatement()).willReturn(statement);
    }

    @Test
    public void shouldExecuteScriptCommand() throws SQLException {
        // given
        final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));

        // when
        databaseScriptCreator.replaceOldScriptFileWithNewOne(tmpPath);

        // then
        verify(statement).execute(Matchers.contains("SCRIPT"));
        verify(statement).close();
    }

    @Test(expected = IllegalStateException.class)
    @SuppressWarnings("unchecked")
    public void shouldThrowIllegalStateExceptionWhenExecutingScriptCommandFails() throws SQLException {
        // given
        given(statement.execute(anyString())).willThrow(SQLException.class);
        final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"));

        // when
        databaseScriptCreator.replaceOldScriptFileWithNewOne(tmpPath);
    }

    @Test
    public void shouldDeleteOldScriptFileBeforeCreatingANewOne() {
        // given
        final File tmpFile = temporaryFile();

        // when
        databaseScriptCreator.replaceOldScriptFileWithNewOne(tmpFile.toPath());

        // then
        assertThat(tmpFile.exists()).isFalse();
    }

    private File temporaryFile() {
        final File tmpFile = Files.newTemporaryFile();
        assertThat(tmpFile.exists()).isTrue();
        return tmpFile;
    }
}
