package com.piotrglazar.nightowl.configuration;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.piotrglazar.nightowl.api.DatabaseFromScriptReader;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component("databaseFromScriptReader")
@Profile("default")
public class DefaultDatabaseFromScriptReader implements DatabaseFromScriptReader {

    private final DataSource dataSource;
    private final StarInfoRepository starInfoRepository;

    @Autowired
    public DefaultDatabaseFromScriptReader(DataSource dataSource, StarInfoRepository starInfoRepository) {
        this.dataSource = dataSource;
        this.starInfoRepository = starInfoRepository;
    }

    @Override
    public void createDatabaseFromScript() {
        if (starInfoRepository.count() == 0) {
            final List<String> allLines = getScriptStatements();
            executeScriptStatements(dataSource, allLines);
        }
    }

    private void executeScriptStatements(final DataSource dataSource, final List<String> allLines) {
        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement()) {
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
