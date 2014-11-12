package com.piotrglazar.nightowl.importers;

import com.google.common.collect.Sets;
import com.piotrglazar.nightowl.util.wrappers.FileReader;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(JUnitParamsRunner.class)
public class ImportedLineFixerTest {

    private FileReader fileReader;

    private ImportedLineFixer importedLineFixer;

    @Before
    public void setUp() throws Exception {
        fileReader = Mockito.mock(FileReader.class);
    }

    @Test
    @Parameters({
            "remove me | true",
            "RemoVe Me | true",
            "remove | false"
    })
    public void shouldMarkLineToBeRemoved(String line, boolean expectedResult) {
        // given
        given(fileReader.getNotEmptyLines("replace")).willReturn(Sets.newHashSet());
        given(fileReader.getNotEmptyLines("skip")).willReturn(Sets.newHashSet("remove me"));
        importedLineFixer = new ImportedLineFixer("replace", "skip", fileReader);

        // when
        final boolean result = importedLineFixer.shouldBeRemoved(line);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @Parameters({
            "replace | content",
            "ReplaCe | content",
            "leave | leave"
    })
    public void shouldReplaceLinesOrLeaveUnchangedIfPossible(String line, String expectedResult) {
        // given
        given(fileReader.getNotEmptyLines("skip")).willReturn(Sets.newHashSet());
        given(fileReader.getNotEmptyLines("replace")).willReturn(Sets.newHashSet("replace->content"));
        importedLineFixer = new ImportedLineFixer("replace", "skip", fileReader);

        // when
        final String result = importedLineFixer.getReplacement(line);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @Parameters({
            "replace | true",
            "ReplaCe | true",
            "leave | false"
    })
    public void shouldContainReplacementForLine(String line, boolean expectedResult) {
        // given
        given(fileReader.getNotEmptyLines("skip")).willReturn(Sets.newHashSet());
        given(fileReader.getNotEmptyLines("replace")).willReturn(Sets.newHashSet("replace->content"));
        importedLineFixer = new ImportedLineFixer("replace", "skip", fileReader);

        // when
        final boolean result = importedLineFixer.shouldBeReplaced(line);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }
}
