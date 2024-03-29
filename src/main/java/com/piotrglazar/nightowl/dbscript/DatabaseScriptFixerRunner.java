package com.piotrglazar.nightowl.dbscript;

import com.piotrglazar.nightowl.configuration.DatabaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Profile("dbScript")
public class DatabaseScriptFixerRunner {

    private final DatabaseScriptFileLocation databaseLocation;
    private final DatabaseScriptCreator databaseScriptCreator;
    private final DatabaseScriptFixer databaseScriptFixer;

    @Autowired
    public DatabaseScriptFixerRunner(DatabaseScriptFileLocation databaseLocation, DatabaseScriptCreator databaseScriptCreator,
                                     DatabaseScriptFixer databaseScriptFixer) {
        this.databaseLocation = databaseLocation;
        this.databaseScriptCreator = databaseScriptCreator;
        this.databaseScriptFixer = databaseScriptFixer;
    }

    public void prepareDbScript() {
        final Path databaseScriptFileLocation = databaseLocation.getDatabaseScriptFileLocation();

        databaseScriptCreator.replaceOldScriptFileWithNewOne(databaseScriptFileLocation);
        databaseScriptFixer.fixScriptFile(databaseScriptFileLocation);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext(DatabaseConfiguration.class);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.setParent(parentContext);
        applicationContext.getEnvironment().setActiveProfiles("dbScript");
        applicationContext.register(DatabaseScriptConfiguration.class);
        applicationContext.refresh();

        applicationContext.getBean(DatabaseScriptFixerRunner.class)
                .prepareDbScript();
    }
}
