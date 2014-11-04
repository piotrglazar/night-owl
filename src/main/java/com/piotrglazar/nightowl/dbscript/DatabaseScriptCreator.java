package com.piotrglazar.nightowl.dbscript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Profile("dbScript")
public class DatabaseScriptCreator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DataSource dataSource;

    @Autowired
    public DatabaseScriptCreator(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void deleteOldScriptFileIfExists(final Path databaseScriptFileLocation) {
        final File databaseFile = databaseScriptFileLocation.toFile();
        if (!databaseFile.delete() && databaseFile.exists()) {
            LOG.error("Failed to delete old database file");
        }
    }

    private void createScriptFile(final Path databaseScriptFileLocation) {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            statement.execute("SCRIPT '" + databaseScriptFileLocation + "';");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to database", e);
        }
    }

    public void replaceOldScriptFileWithNewOne(final Path databaseScriptFileLocation) {
        deleteOldScriptFileIfExists(databaseScriptFileLocation);
        createScriptFile(databaseScriptFileLocation);
    }
}
