package com.piotrglazar.nightowl.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseFromScriptReaderTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private DatabaseLocation databaseLocation;

    @InjectMocks
    private DefaultDatabaseFromScriptReader reader;

    @Test
    public void shouldNotExecuteScriptWhenDatabaseIsAlreadyCreated() {
        // when
        reader.createDatabaseFromScript();

        // then
        verify(databaseLocation, never()).getDatabaseScriptLocation();
    }

    @Test
    public void shouldCreateDatabaseFromScript() throws SQLException {
        // given
        given(databaseLocation.databaseNeedsCreation()).willReturn(true);
        final Statement statement = connectionStatement();

        // when
        reader.createDatabaseFromScript();

        // then
        verify(statement, atLeastOnce()).execute(anyString());
        verify(statement).close();
    }

    private Statement connectionStatement() throws SQLException {
        Connection connection = mock(Connection.class);
        given(dataSource.getConnection()).willReturn(connection);
        Statement statement = mock(Statement.class);
        given(connection.createStatement()).willReturn(statement);
        return statement;
    }
}
