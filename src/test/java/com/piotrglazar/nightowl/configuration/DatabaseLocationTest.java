package com.piotrglazar.nightowl.configuration;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseLocationTest {

    private DatabaseLocation databaseLocation = new DatabaseLocation();

    @Test
    public void shouldProductionDatabaseBeLocatedInDbProduction() {
        // given
        final String expectedLocationSuffix = "db/production";

        // when
        final String location = databaseLocation.getDatabaseLocation();

        // then
        assertThat(location).endsWith(expectedLocationSuffix);
    }

    @Test
    public void shouldProductionDatabaseScriptEndWithScriptSuffix() {
        // given
        final String expectedScriptSuffix = ".script";

        // when
        final String databaseScriptLocation = databaseLocation.getDatabaseScriptLocation();

        // then
        assertThat(databaseScriptLocation).endsWith(expectedScriptSuffix);
    }

    @Test
    @Ignore("Run only when there is no production database created (the app wasn't run).")
    public void shouldProductionDatabaseNeedCreation() {
        // when
        final boolean needsCreation = databaseLocation.databaseNeedsCreation();

        // then
        assertThat(needsCreation).isTrue();
    }

    @Test
    @Ignore("Run only when there is no production database created (the app wasn't run).")
    public void shouldProductionDatabaseNotNeedCreation() throws IOException {
        // given
        Files.createFile(Paths.get(databaseLocation.getDatabaseScriptLocation()));

        // when
        final boolean needsCreation = databaseLocation.databaseNeedsCreation();

        // then
        assertThat(needsCreation).isFalse();

        // cleanup
        Files.delete(Paths.get(databaseLocation.getDatabaseScriptLocation()));
    }
}
