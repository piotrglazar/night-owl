package com.piotrglazar.nightowl.configuration;

import java.nio.file.Paths;

public class DatabaseLocation {

    public String getDatabaseRootLocation() {
        return ApplicationConfiguration.getCurrentDirectory() + "/database";
    }

    public String getDatabaseLocation() {
        return getDatabaseRootLocation() + "/production";
    }

    public String getDatabaseScriptLocation() {
        return getDatabaseLocation() + ".script";
    }

    public boolean databaseNeedsCreation() {
        return !Paths.get(getDatabaseScriptLocation()).toFile().exists();
    }
}
