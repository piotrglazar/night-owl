package com.piotrglazar.nightowl.dbscript;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.util.wrappers.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile("dbScript")
public class DatabaseScriptFixer {

    private final Set<String> allowedLines;
    private final Set<String> scriptTailLines;
    private final FileReader fileReader;

    @Autowired
    public DatabaseScriptFixer(FileReader fileReader, @Value("db/allowed.lines.txt") String allowedLinesPath,
                               @Value("db/script.tail.lines.txt") String tailLinesPath) {
        this.fileReader = fileReader;
        this.allowedLines = fileReader.getNotEmptyLines(allowedLinesPath);
        this.scriptTailLines = fileReader.getNotEmptyLines(tailLinesPath);
    }

    public void fixScriptFile(final Path databaseScriptFileLocation) {
        final List<String> scriptTail = Lists.newLinkedList();
        final List<String> filteredScriptLines = fileReader.getNotEmptyLines(databaseScriptFileLocation)
                .filter(s -> allowedLines.stream().anyMatch(s::contains))
                .filter(s -> {
                    if (scriptTailLines.stream().filter(s::contains).count() > 0) {
                        scriptTail.add(s);
                        return false;
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
        filteredScriptLines.addAll(scriptTail);
        writeFixedScript(databaseScriptFileLocation, filteredScriptLines);
    }

    private void writeFixedScript(final Path databaseScriptFileLocation, final List<String> filteredScriptLines) {
        try {
            Files.write(databaseScriptFileLocation, filteredScriptLines, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
