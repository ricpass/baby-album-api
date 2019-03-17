package com.ricardopassarella.nbrown.babyalbum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
class BabyFileHandler {

    private final String localStoragePath;

    public BabyFileHandler(@Value("${baby-album.localstorage.path:}") String path) {
        if (path.isEmpty()) {
            try {
                Path tempDirectory = Files.createTempDirectory("baby-album-local-storage");
                tempDirectory.toFile().deleteOnExit();
                this.localStoragePath = tempDirectory.toString();
                log.info("Created temporary local storage at " + tempDirectory.toString());
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create temporary local storage", e);
            }
        } else {
            if (new File(path).exists()) {
                this.localStoragePath = path;
            } else {
                throw new IllegalStateException("Failed to setup local storage.");
            }
        }
    }

    File save(byte[] bytes, String fileName) {
        try {
            File file = new File(localStoragePath + "/" + fileName);
            Files.write(file.toPath(), bytes);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    byte[] readImageFromFile(String fileName) {
        try {
            Path path = Paths.get(localStoragePath + "/" + fileName);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    boolean delete(String fileName) {
        try {
            Path path = Paths.get(localStoragePath + "/" + fileName);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
