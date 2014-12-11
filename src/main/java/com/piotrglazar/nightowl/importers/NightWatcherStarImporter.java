package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.api.StarInfoProvider;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.StarInfoDetails;
import com.piotrglazar.nightowl.util.wrappers.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Profile("importing")
public class NightWatcherStarImporter {

    private static final Logger LOG = LoggerFactory.getLogger("STAR_IMPORTER");
    public static final double APPARENT_MAG_FACTOR = 1000.0;

    private final StarInfoProvider starInfoProvider;
    private final ImportedLineFixer importedLineFixer;
    private final FileReader fileReader;

    @Autowired
    public NightWatcherStarImporter(StarInfoProvider starInfoProvider, ImportedLineFixer importedLineFixer, FileReader fileReader) {
        this.starInfoProvider = starInfoProvider;
        this.importedLineFixer = importedLineFixer;
        this.fileReader = fileReader;
    }

    public void importStars(final Path path) {
        LOG.info("Delete previous stars");
        starInfoProvider.deleteAll();

        fileReader.getNotEmptyLines(path)
                .map(String::trim)
                .map(line -> line.replace(',', '.'))
                .map(line -> line.split(" +"))
                .map(shards -> new StarInfo(rightAscension(shards), declination(shards), spectralType(shards), starDetailsIfExist(shards),
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

    private StarInfoDetails starDetailsIfExist(final String[] shards) {
        if (shards.length >= 14) {
            return getStarInfoDetailsIfPossible(shards);
        } else {
            // explicit null, handled by optional inside StarInfo
            return null;
        }
    }

    private StarInfoDetails getStarInfoDetailsIfPossible(final String[] shards) {
        final String starNameFixed = starNameFixed(getRemainingShardsAsStarName(shards));
        if (!starNameFixed.isEmpty()) {
            return new StarInfoDetails(starNameFixed);
        } else {
            // explicit null, handled by optional inside StarInfo
            return null;
        }
    }

    private String starNameFixed(final String starName) {
        if (importedLineFixer.shouldBeRemoved(starName)) {
            return "";
        } else if (importedLineFixer.shouldBeReplaced(starName)) {
            return importedLineFixer.getReplacement(starName);
        } else {
            return starName;
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
