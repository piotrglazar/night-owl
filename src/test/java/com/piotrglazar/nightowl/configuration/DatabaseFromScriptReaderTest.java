package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.model.StarInfoRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    private StarInfoRepository starInfoRepository;

    @InjectMocks
    private DefaultDatabaseFromScriptReader reader;

    @Test
    @SuppressFBWarnings
    public void shouldNotExecuteScriptWhenDatabaseIsAlreadyCreated() throws SQLException {
        // given
        given(starInfoRepository.count()).willReturn(someStarInfoEntriesInDatabase());

        // when
        reader.createDatabaseFromScript();

        // then
        verify(dataSource, never()).getConnection();
    }

    @Test
    @SuppressFBWarnings
    public void shouldCreateDatabaseFromScript() throws SQLException {
        // given
        final Statement statement = connectionStatement();

        // when
        reader.createDatabaseFromScript();

        // then
        verify(statement, atLeastOnce()).execute(anyString());
        verify(statement).close();
    }

    private long someStarInfoEntriesInDatabase() {
        return 1;
    }

    private Statement connectionStatement() throws SQLException {
        Connection connection = mock(Connection.class);
        given(dataSource.getConnection()).willReturn(connection);
        Statement statement = mock(Statement.class);
        given(connection.createStatement()).willReturn(statement);
        return statement;
    }
}
