package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.model.entities.StarInfo;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class NightWatcherStarImporter {

    private static final Logger LOG = LoggerFactory.getLogger("STAR_IMPORTER");
    public static final double APPARENT_MAG_FACTOR = 1000.0;

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
            throw new IllegalArgumentException("Expecting path to star file (cat_mis_65_eng.txt) as the first argument");
        }
    }

    public void importStars(final Path path) throws IOException {
        LOG.info("Delete previous stars");
        starInfoProvider.deleteAll();

        Files.lines(path)
                .map(String::trim)
                .map(line -> line.replace(',', '.'))
                .map(line -> line.split(" +"))
                .map(shards -> new StarInfo(rightAscension(shards), declination(shards), spectralType(shards), starNameIfExists(shards),
                        apparentMagnitude(shards)))
                .forEach(starInfoProvider::saveStarInfo);
        LOG.info("Imported stars: {}",  starInfoProvider.count());
    }

    private double apparentMagnitude(final String[] shards) {
        return getDouble(shards[2]) / APPARENT_MAG_FACTOR;
    }

    private String spectralType(final String[] shards) {
        return shards[5];
    }

    private double declination(final String[] shards) {
        return getDouble(shards[1]);
    }

    private double getDouble(final String rawDouble) {
        return Double.parseDouble(rawDouble);
    }

    private LocalTime rightAscension(final String[] shards) {
        return convertTimeAsFractionalPartOfHourToLocalTime(shards[0]);
    }

    private String starNameIfExists(final String[] shards) {
        if (shards.length >= 14) {
            return getRemainingShardsAsStarName(shards);
        } else {
            return "";
        }
    }

    private String getRemainingShardsAsStarName(final String[] shards) {
        return IntStream.range(13, shards.length).mapToObj(i -> shards[i]).collect(Collectors.joining(" "));
    }

    private LocalTime convertTimeAsFractionalPartOfHourToLocalTime(final String doubleRepresentation) {
        final double rawRightAscension = getDouble(doubleRepresentation);
        final double rightAscensionAsSecondsOfDay = (rawRightAscension / 24.0) * 86400;

        return LocalTime.ofSecondOfDay((long) rightAscensionAsSecondsOfDay);
    }
}
