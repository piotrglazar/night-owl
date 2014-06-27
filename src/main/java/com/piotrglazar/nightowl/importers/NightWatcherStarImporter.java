package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.model.StarInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

@Component
public class NightWatcherStarImporter {

    private static final Logger LOG = LoggerFactory.getLogger("STAR_IMPORTER");

    private final StarInfoProvider starInfoProvider;

    @Autowired
    public NightWatcherStarImporter(final StarInfoProvider starInfoProvider) {
        this.starInfoProvider = starInfoProvider;
    }

    public static void main(String[] args) throws IOException {
        verifyArguments(args);

        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        applicationContext.registerShutdownHook();

        final Path starsFilePath = Paths.get(args[0]);
        LOG.info("Importing stars from {}", starsFilePath);
        applicationContext.getBean(NightWatcherStarImporter.class).importStars(starsFilePath);
        LOG.info("Done importing stars");
        System.exit(0);
    }

    private static void verifyArguments(final String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Expecting path to star file as the first argument");
        }
    }

    public void importStars(final Path path) throws IOException {
        LOG.info("Delete previous stars");
        starInfoProvider.deleteAll();

        Files.lines(path)
                .map(String::trim)
                .map(line -> line.replace(',', '.'))
                .map(line -> line.split(" +"))
                .map(shards ->
                        new StarInfo(convertTimeAsFractionalPartOfHourToLocalTime(shards[0]), Double.parseDouble(shards[1]), shards[5]))
                .forEach(starInfoProvider::saveStarInfo);
        LOG.info("Imported stars: {}",  starInfoProvider.count());
    }

    private LocalTime convertTimeAsFractionalPartOfHourToLocalTime(String doubleRepresentation) {
        final double rawRightAscension = Double.parseDouble(doubleRepresentation);
        final double rightAscensionAsSecondsOfDay = (rawRightAscension / 24.0) * 86400;

        return LocalTime.ofSecondOfDay((long) rightAscensionAsSecondsOfDay);
    }
}
