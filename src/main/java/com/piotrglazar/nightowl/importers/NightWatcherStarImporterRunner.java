package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.configuration.DatabaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NightWatcherStarImporterRunner {

    private static final Logger LOG = LoggerFactory.getLogger("STAR_IMPORTER");

    private NightWatcherStarImporterRunner() {
        // Utility class
    }

    public static void main(String[] args) throws IOException {
        verifyArguments(args);

        new NightWatcherStarImporterRunner().run(Paths.get(args[0]));
    }

    private void run(final Path starsFilePath) {
        LOG.info("Importing stars from {}", starsFilePath);
        final AnnotationConfigApplicationContext applicationContext = getApplicationContext();
        applicationContext.getBean(NightWatcherStarImporter.class).importStars(starsFilePath);
        LOG.info("Done importing stars");
    }

    private AnnotationConfigApplicationContext getApplicationContext() {
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("importing");
        applicationContext.register(ImporterConfiguration.class, DatabaseConfiguration.class);
        applicationContext.refresh();
        applicationContext.registerShutdownHook();
        return applicationContext;
    }

    private static void verifyArguments(final String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Expecting path to star file (cat_mis_65_eng.txt) as the first argument");
        }
    }
}
