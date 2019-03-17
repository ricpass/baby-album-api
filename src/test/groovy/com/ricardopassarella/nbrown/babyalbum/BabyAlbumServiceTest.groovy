package com.ricardopassarella.nbrown.babyalbum

import com.ricardopassarella.nbrown.GeoLocation.ReverseGeocoding
import com.ricardopassarella.nbrown.baby.BabyFacade
import com.ricardopassarella.nbrown.babyalbum.dto.PictureMetadata
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files
import java.nio.file.Paths

class BabyAlbumServiceTest extends Specification {

    def repository = Mock BabyAlbumRepository
    def metadataExtractor = Mock MetadataExtractor
    def fileHandler = Mock BabyFileHandler
    def babyFacade = Mock BabyFacade
    def reverseGeocoding = Mock ReverseGeocoding

    @Subject
    BabyAlbumService service

    void setup() {
        service = new BabyAlbumService(
                repository,
                metadataExtractor,
                fileHandler,
                babyFacade,
                reverseGeocoding,
                2)
    }

    def "when upload image as multipartFile"() {
        given:
        def file = MetadataExtractorTest.class.getResource("/image-with-location-data.jpg").getFile()
        byte[] imageBytes = Files.readAllBytes(Paths.get(file))
        def multipartFile = new MockMultipartFile("file", imageBytes)
        def clientId = "e0b9e335251a74cad293597d6b0d8341"
        def imageId = "0dde80fc97fe48b49cb3bd61e09d8ad9"
        def fullAddress = "Picadilly Gardens"

        def metadata = PictureMetadata.builder().build()

        when:
        def response = service.processImage(multipartFile, clientId)

        then: "read file metadata"
        1 * metadataExtractor.read(imageBytes) >> metadata

        and: "get full address reverse geocoding"
        1 * reverseGeocoding.getFullAddress(metadata.getLatitude(), metadata.getLongitude()) >> Optional.of("Picadilly Gardens")

        and: "save details on the database"
        1 * repository.insert(clientId, metadata, fullAddress) >> imageId

        and: "save image on local storage"
        1 * fileHandler.save(imageBytes, imageId)

        and: "return imageId"
        response.getImageId() == imageId
    }

    def "when upload image encoded base64"() {
        given:
        def file = MetadataExtractorTest.class.getResource("/image-with-location-data.jpg").getFile()
        byte[] imageBytes = Files.readAllBytes(Paths.get(file))

        String imageEncoded = Base64.getEncoder().encodeToString(imageBytes)

        def clientId = "e0b9e335251a74cad293597d6b0d8341"
        def imageId = "0dde80fc97fe48b49cb3bd61e09d8ad9"

        def metadata = PictureMetadata.builder().build()

        when:
        def response = service.processImage(imageEncoded, clientId)

        then: "read file metadata"
        1 * metadataExtractor.read(imageBytes) >> metadata

        and: "get full address reverse geocoding"
        1 * reverseGeocoding.getFullAddress(metadata.getLatitude(), metadata.getLongitude()) >> Optional.empty()

        and: "save details on the database"
        1 * repository.insert(clientId, metadata, null) >> imageId

        and: "save image on local storage"
        1 * fileHandler.save(imageBytes, imageId)

        and: "return imageId"
        response.getImageId() == imageId
    }


}
