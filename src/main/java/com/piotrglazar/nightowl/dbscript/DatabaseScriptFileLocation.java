package com.piotrglazar.nightowl.dbscript;

import com.piotrglazar.nightowl.configuration.DatabaseLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Profile("dbScript")
public class DatabaseScriptFileLocation {

    private static final String DATABASE_SCRIPT_FILE_NAME = "database.sql";

    private final DatabaseLocation databaseLocation;

    @Autowired
    public DatabaseScriptFileLocation(final DatabaseLocation databaseLocation) {
        this.databaseLocation = databaseLocation;
    }

    public Path getDatabaseScriptFileLocation() {
        return Paths.get(databaseLocation.getDatabaseRootLocation(), DATABASE_SCRIPT_FILE_NAME);
    }
}
