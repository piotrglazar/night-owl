package com.piotrglazar.nightowl.dbscript;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile("dbScript")
public class FileReader {

    public Stream<String> getNotEmptyLines(final Path databaseScriptFileLocation) {
        try {
            return Files.readAllLines(databaseScriptFileLocation, Charsets.UTF_8)
                    .stream()
                    .filter(s -> !s.isEmpty());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Set<String> getNotEmptyLines(String location) {
        try {
            final URL resource = Resources.getResource(location);
            final Stream<String> allLines = getNotEmptyLines(Paths.get(resource.toURI()));
            return allLines.collect(Collectors.toSet());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
