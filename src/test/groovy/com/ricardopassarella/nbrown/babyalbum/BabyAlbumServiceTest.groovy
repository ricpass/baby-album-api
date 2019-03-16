package com.ricardopassarella.nbrown.babyalbum

import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class BabyAlbumServiceTest extends Specification {

    BabyAlbumService service

    void setup() {
        service = new BabyAlbumService(
                Mock(BabyAlbumRepository),
                Mock(MetadataExtractor),
                Mock(BabyFileHandler))
    }

//    def "bla"() {
//        given:
//        def file = BabyAlbumServiceTest.class.getResource("/image-with-location-data.jpg").getFile()
//
//        Path path = Paths.get(file)
//        def data = Files.readAllBytes(path)
//
//        when:
//        String imageEncoded = Base64.getEncoder().encodeToString(data)
//        println(imageEncoded)
//
//        byte[] imageByte = Base64.getDecoder().decode(imageEncoded.getBytes(StandardCharsets.UTF_8))
//
//
//        service.processImage(imageEncoded, "bla")
//
//        then:
//        imageByte == data
//
//    }

}
