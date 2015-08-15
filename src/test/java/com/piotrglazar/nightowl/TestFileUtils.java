package com.piotrglazar.nightowl;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface TestFileUtils {

    default String tmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    default Path tmpDirPath() {
        return Paths.get(tmpDir());
    }

    default Path tmpTextFile() throws IOException {
        return Files.createTempFile("", ".txt").toAbsolutePath();
    }

    default void deleteFile(Path path) {
        if (!path.toFile().delete()) {
            throw new IllegalStateException("Failed to delete " + path);
        }
    }

    default Path resourcePath(String path) throws IOException {
        return Paths.get(new ClassPathResource(path).getFile().getAbsolutePath());
    }
}
