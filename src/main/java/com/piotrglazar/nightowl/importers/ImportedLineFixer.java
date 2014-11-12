package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.util.wrappers.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@Component
@Profile("importing")
public class ImportedLineFixer {

    private final Set<String> linesToBeRemoved;
    private final Map<String, String> linesToBeReplaced;

    @Autowired
    public ImportedLineFixer(@Value("importers/replace.lines.txt") String replaceLinesPath,
                             @Value("importers/skip.lines.txt") String skipLinesPath,
                             FileReader fileReader) {
        this.linesToBeRemoved = linesToBeRemoved(fileReader, skipLinesPath);
        this.linesToBeReplaced = linesToBeReplaced(fileReader, replaceLinesPath);
    }

    private Map<String, String> linesToBeReplaced(final FileReader fileReader, final String skipLinesPath) {
        return fileReader.getNotEmptyLines(skipLinesPath).stream().map(s -> s.split("->")).collect(toMap(s -> s[0], s -> s[1]));
    }

    private Set<String> linesToBeRemoved(final FileReader fileReader, final String skipLinesPath) {
        return fileReader.getNotEmptyLines(skipLinesPath);
    }

    public boolean shouldBeRemoved(String line) {
        return linesToBeRemoved.contains(line.toLowerCase());
    }

    public boolean shouldBeReplaced(String line) {
        return linesToBeReplaced.containsKey(line.toLowerCase());
    }

    public String getReplacement(String line) {
        return linesToBeReplaced.getOrDefault(line.toLowerCase(), line);
    }
}
