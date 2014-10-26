package com.piotrglazar.nightowl.configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@Profile("default")
public class DatabaseLocation {

    public String getDatabaseLocation() {
        return ApplicationConfiguration.getCurrentDirectory() + "/db/production";
    }

    public String getDatabaseScriptLocation() {
        return getDatabaseLocation() + ".script";
    }

    public boolean databaseNeedsCreation() {
        return !Paths.get(getDatabaseScriptLocation()).toFile().exists();
    }
}
