package com.ricardopassarella.nbrown.babyalbum

import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files
import java.nio.file.Paths

class BabyFileHandlerTest extends Specification {

    @Subject
    BabyFileHandler fileHandler

    void setup() {
        fileHandler = new BabyFileHandler("")
    }

    def "test file create and read"() {
        given:
        def file = MetadataExtractorTest.class.getResource("/image-with-location-data.jpg").getFile()
        byte[] imageBytes = Files.readAllBytes(Paths.get(file))

        when:
        fileHandler.save(imageBytes, "test")
        def imageByteRead = fileHandler.readImageFromFile("test")

        then:
        imageByteRead == imageBytes

        and:
        fileHandler.delete("test")
    }
}
