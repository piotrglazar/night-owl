package com.piotrglazar.nightowl.dbscript;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FileReaderTest {

    private FileReader fileReader = new FileReader();

    @Test
    public void shouldReadFileAndSkipEmptyLines() {
        // given
        final String location = "dbscript/fileReader.txt";

        // when
        final Set<String> lines = fileReader.getNotEmptyLines(location);

        // then
        assertThat(lines).containsOnly("this is not empty line", "another not empty", "hello world");
    }
}
