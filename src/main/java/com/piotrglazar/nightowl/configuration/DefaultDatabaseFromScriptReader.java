package com.piotrglazar.nightowl.configuration;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.piotrglazar.nightowl.DatabaseFromScriptReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component("databaseFromScriptReader")
@Profile("default")
public class DefaultDatabaseFromScriptReader implements DatabaseFromScriptReader {

    private final DataSource dataSource;
    private final DatabaseLocation databaseLocation;

    @Autowired
    public DefaultDatabaseFromScriptReader(DataSource dataSource, DatabaseLocation databaseLocation) {
        this.dataSource = dataSource;
        this.databaseLocation = databaseLocation;
    }

    @Override
    @PostConstruct
    public void createDatabaseFromScript() {
        if (databaseLocation.databaseNeedsCreation()) {
            final List<String> allLines = getScriptStatements();
            executeScriptStatements(dataSource, allLines);
        }
    }

    private void executeScriptStatements(final DataSource dataSource, final List<String> allLines) {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            for (String line : allLines) {
                statement.execute(line);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to execute script", e);
        }
    }

    private List<String> getScriptStatements() {
        try {
            final URI databaseScript = Resources.getResource("db/database.sql").toURI();
            return Files.readAllLines(Paths.get(databaseScript), Charsets.UTF_8);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Failed to read database script file", e);
        }
    }
}
