package com.ricardopassarella.nbrown.babyalbum;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
class BabyFileHandler {

    private final String localStoragePath;

    public BabyFileHandler() {
        try {
            Path tempDirectory = Files.createTempDirectory("baby-album-local-storage");
            this.localStoragePath = tempDirectory.toString();
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
            throw new RuntimeException("Failed to read file");
        }
    }

}
