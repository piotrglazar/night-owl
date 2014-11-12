package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.DatabaseFromScriptReader;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("databaseFromScriptReader")
@Profile("importing")
public class NoOpDatabaseFromScriptReader implements DatabaseFromScriptReader {

    @Override
    public void createDatabaseFromScript() {
        // Do nothing, we are importing stars from an external source.
        // There is no need to execute script to load db
    }
}
