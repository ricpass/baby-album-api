package com.ricardopassarella.nbrown.babyalbum;

import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumDto;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumResponse;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumResponseWithLinks;
import com.ricardopassarella.nbrown.babyalbum.dto.PictureMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class BabyAlbumService {

    private final BabyAlbumRepository repository;
    private final MetadataExtractor metadataExtractor;
    private final BabyFileHandler fileHandler;

    void processImage(MultipartFile multipartFile, String clientId) {
        try {
            byte[] imageByte = multipartFile.getBytes();

            processImage(imageByte, clientId);
        } catch (IOException e) {
            throw new RuntimeException("failed to process image", e);
        }
    }

    void processImage(String imageBase64, String clientId) {
        byte[] imageByte = Base64.getDecoder().decode(imageBase64.getBytes(StandardCharsets.UTF_8));

        processImage(imageByte, clientId);
    }

    private void processImage(byte[] imageBytes, String clientId) {
        PictureMetadata metadata = metadataExtractor.read(imageBytes);

        String imageId = repository.insert(clientId, metadata);

        fileHandler.save(imageBytes, imageId);
    }

    Optional<byte[]> getImage(String imageId, String clientId) {
        Optional<BabyAlbumDto> babyAlbumDto = repository.findImageById(imageId, clientId);

        return babyAlbumDto
                .map(dto -> fileHandler.readImageFromFile(dto.getId()));
    }

    Optional<BabyAlbumResponse> getImageAsBase64(String imageId, String clientId) {
        return repository.findImageById(imageId, clientId)
                .map(this::createBabyAlbumResponse);
    }

    List<BabyAlbumResponse> getImagesAsBase64(String clientId) {
        return repository.findImagesByClientId(clientId)
                .stream()
                .map(this::createBabyAlbumResponse)
                .collect(Collectors.toList());
    }

    private BabyAlbumResponse createBabyAlbumResponse(BabyAlbumDto dto) {
        byte[] imageBytes = fileHandler.readImageFromFile(dto.getId());
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        return BabyAlbumResponse.builder()
                .id(dto.getId())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .dateTime(dto.getDateTime())
                .imageBase64(encodedImage)
                .build();
    }

    List<BabyAlbumResponseWithLinks> getImagesAsLinks(String clientId) {
        return repository.findImagesByClientId(clientId)
                .stream()
                .map(dto -> BabyAlbumResponseWithLinks.builder()
                        .id(dto.getId())
                        .latitude(dto.getLatitude())
                        .longitude(dto.getLongitude())
                        .dateTime(dto.getDateTime())
                        .url(getPathToGetImageMethod(dto.getId(), dto.getClientId()))
                        .build())
                .collect(Collectors.toList());
    }

    private String getPathToGetImageMethod(String id, String clientId) {
        ControllerLinkBuilder linkBuilder =
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder.methodOn(BabyAlbumController.class)
                                .getImageEncoded(id, clientId));

        URI uri = linkBuilder.toUri();
        return uri.getPath();
    }
}
