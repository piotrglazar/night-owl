package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.StarInfoProvider;
import com.piotrglazar.nightowl.model.StarInfo;
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

    private final StarInfoProvider starInfoProvider;

    @Autowired
    public NightWatcherStarImporter(final StarInfoProvider starInfoProvider) {
        this.starInfoProvider = starInfoProvider;
    }

    public static void main(String[] args) throws IOException {
        verifyArguments(args);

        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        applicationContext.registerShutdownHook();

        applicationContext.getBean(NightWatcherStarImporter.class).importStars(Paths.get(args[0]));
        System.exit(0);
    }

    private static void verifyArguments(final String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Expecting path to star file as the first argument");
        }
    }

    public void importStars(final Path path) throws IOException {
        Files.lines(path)
                .map(line -> line.trim())
                .map(line -> line.replace(',', '.'))
                .map(line -> line.split(" +"))
                .map(shards ->
                        new StarInfo(convertTimeAsFractionalPartOfHourToLocalTime(shards[0]), Double.parseDouble(shards[1]), shards[5]))
                .forEach(starInfo -> starInfoProvider.saveStarInfo(starInfo));
        System.out.println("Imported stars: " + starInfoProvider.getAllStars().size());
    }

    private LocalTime convertTimeAsFractionalPartOfHourToLocalTime(String doubleRepresentation) {
        final double rawRightAscension = Double.parseDouble(doubleRepresentation);
        final double rightAscensionAsSecondsOfDay = (rawRightAscension / 24.0) * 86400;

        return LocalTime.ofSecondOfDay((long) rightAscensionAsSecondsOfDay);
    }
}
