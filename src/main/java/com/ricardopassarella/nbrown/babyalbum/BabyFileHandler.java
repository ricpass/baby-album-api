package com.ricardopassarella.nbrown.babyalbum;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
class BabyFileHandler {

    private final String localStoragePath;

    public BabyFileHandler() {
        try {
            Path tempDirectory = Files.createTempDirectory("baby-album-local-storage");
            this.localStoragePath = tempDirectory.toString();
            log.info("Created temporary local storage at " + tempDirectory.toString());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create temporary local storage", e);
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

    @PreDestroy
    public void cleanup() {
        try {
            log.info("Deleting directory " + localStoragePath);
            FileUtils.deleteDirectory(new File(localStoragePath));
        } catch (IOException e) {
            log.error("Failed to delete temporary local storage", e);
        }
    }

}
