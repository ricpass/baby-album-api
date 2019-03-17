package com.ricardopassarella.nbrown.babyalbum

import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static java.math.RoundingMode.HALF_UP

class MetadataExtractorTest extends Specification {

    @Subject
    static MetadataExtractor extractor

    void setup() {
        extractor = new MetadataExtractor()
    }

    def "Read #fileName"() {
        given:
        def file = MetadataExtractorTest.class.getResource(fileName).getFile()
        byte[] imageBytes = Files.readAllBytes(Paths.get(file))
        def dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        when:
        def metadata = extractor.read(imageBytes)

        then:
        metadata.getLongitude().setScale(4, HALF_UP) == expectedLongitude
        metadata.getLatitude().setScale(4, HALF_UP) == expectedLatitude
        metadata.getLocalDateTime() == LocalDateTime.parse(expectedDateTime, dateTimeFormatter)

        where:
        fileName                        || expectedLongitude | expectedLatitude | expectedDateTime
        "/image-with-location-data.jpg" || -2.2362           | 53.4827          | "2019-03-15 12:19:56"
    }

    def "Read image without location data"() {
        given:
        def fileName = "/image-without-location-data.jpg"
        def file = MetadataExtractorTest.class.getResource(fileName).getFile()

        byte[] imageBytes = Files.readAllBytes(Paths.get(file))

        when:
        def metadata = extractor.read(imageBytes)

        then:
        metadata.getLatitude() == null
    }
}
