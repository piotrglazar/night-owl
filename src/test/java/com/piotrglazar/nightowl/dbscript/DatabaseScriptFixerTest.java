package com.piotrglazar.nightowl.dbscript;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.piotrglazar.nightowl.TestFileUtils;
import com.piotrglazar.nightowl.util.wrappers.FileReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("all")
public class DatabaseScriptFixerTest implements TestFileUtils {

    @Mock
    private FileReader fileReader;

    private DatabaseScriptFixer databaseScriptFixer;

    @Test
    public void shouldFixScriptByRemovingUnwantedLines() throws IOException {
        // given
        final Path path = tmpTextFile();
        given(fileReader.getNotEmptyLines(path)).willReturn(Lists.newArrayList("line0", "line1", "line2", "line3", "line4").stream());
        given(fileReader.getNotEmptyLines("allowed")).willReturn(Sets.newHashSet("line0", "line4"));
        given(fileReader.getNotEmptyLines("tail")).willReturn(Sets.newHashSet());
        databaseScriptFixer = new DatabaseScriptFixer(fileReader, "allowed", "tail");

        // when
        databaseScriptFixer.fixScriptFile(path);

        // then
        assertThat(Files.readAllLines(path, Charsets.UTF_8)).containsExactly("line0", "line4");

        // cleanup
        deleteFile(path);
    }

    @Test
    public void shouldFixScriptByMovingSpecificLinesToTail() throws IOException {
        // given
        final Path path = tmpTextFile();
        given(fileReader.getNotEmptyLines(path)).willReturn(Lists.newArrayList("line0", "line1", "line2", "line3", "line4").stream());
        given(fileReader.getNotEmptyLines("allowed")).willReturn(Sets.newHashSet("line0", "line1", "line2", "line3", "line4"));
        given(fileReader.getNotEmptyLines("tail")).willReturn(Sets.newHashSet("line1", "line2"));
        databaseScriptFixer = new DatabaseScriptFixer(fileReader, "allowed", "tail");

        // when
        databaseScriptFixer.fixScriptFile(path);

        // then
        assertThat(Files.readAllLines(path, Charsets.UTF_8)).containsExactly("line0", "line3", "line4", "line1", "line2");

        // cleanup
        deleteFile(path);
    }

    @Test
    public void shouldRemovingLineTakePrecedenceOverMovingLineToTail() throws IOException {
        // given
        final Path path = tmpTextFile();
        given(fileReader.getNotEmptyLines(path)).willReturn(Lists.newArrayList("line0", "line1").stream());
        given(fileReader.getNotEmptyLines("allowed")).willReturn(Sets.newHashSet("line1"));
        given(fileReader.getNotEmptyLines("tail")).willReturn(Sets.newHashSet("line0"));
        databaseScriptFixer = new DatabaseScriptFixer(fileReader, "allowed", "tail");

        // when
        databaseScriptFixer.fixScriptFile(path);

        // then
        assertThat(Files.readAllLines(path, Charsets.UTF_8)).containsExactly("line1");

        // cleanup
        deleteFile(path);
    }
}
