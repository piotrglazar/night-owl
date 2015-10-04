package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.entities.StarColor;
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
import java.util.stream.Stream;

@Component
@Profile("importing")
public class NightWatcherStarImporter {

    private static final Logger LOG = LoggerFactory.getLogger("STAR_IMPORTER");
    public static final double APPARENT_MAG_FACTOR = 1000.0;
    public static final int MINIMAL_SHARDS_LENGTH = 9;

    private final StarInfoRepository starInfoRepository;
    private final ImportedLineFixer importedLineFixer;
    private final FileReader fileReader;

    @Autowired
    public NightWatcherStarImporter(StarInfoRepository starInfoRepository, ImportedLineFixer importedLineFixer, FileReader fileReader) {
        this.starInfoRepository = starInfoRepository;
        this.importedLineFixer = importedLineFixer;
        this.fileReader = fileReader;
    }

    public void importStars(final Path path) {
        LOG.info("Delete previous stars");
        starInfoRepository.deleteAll();

        processStars(fileReader.getNotEmptyLines(path)).forEach(starInfoRepository::save);

        LOG.info("Imported stars: {}",  starInfoRepository.count());
    }

    public Stream<StarInfo> processStars(Stream<String> rawLines) {
        return rawLines
                .map(String::trim)
                .map(line -> line.replace(',', '.'))
                .map(line -> line.split(" +"))
                // prune invalid entries (not enough data)
                .filter(shards -> shards.length > MINIMAL_SHARDS_LENGTH)
                // prune invalid star colors (i.e. spectral type is incorrect)
                .filter(shards -> starColor(shards) != null)
                .map(shards -> new StarInfo(rightAscension(shards), declination(shards), spectralType(shards), starDetailsIfExist(shards),
                        apparentMagnitude(shards), starColor(shards)));
    }

    private StarColor starColor(final String[] shards) {
        return StarColor.fromStarSpectralType(spectralType(shards));
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
