package com.ricardopassarella.nbrown.babyalbum;

import com.ricardopassarella.nbrown.baby.BabyFacade;
import com.ricardopassarella.nbrown.baby.dto.BabyResponse;
import com.ricardopassarella.nbrown.babyalbum.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
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

    private final BabyFacade babyFacade;

    @Transactional
    public BabyAlbumUploadResponse processImage(MultipartFile multipartFile, String clientId) {
        try {
            byte[] imageByte = multipartFile.getBytes();

            return processImage(imageByte, clientId);
        } catch (IOException e) {
            throw new RuntimeException("failed to process image", e);
        }
    }

    @Transactional
    public BabyAlbumUploadResponse processImage(String imageBase64, String clientId) {
        byte[] imageByte = Base64.getDecoder().decode(imageBase64.getBytes(StandardCharsets.UTF_8));

        return processImage(imageByte, clientId);
    }

    private BabyAlbumUploadResponse processImage(byte[] imageBytes, String clientId) {
        PictureMetadata metadata = metadataExtractor.read(imageBytes);

        String imageId = repository.insert(clientId, metadata);

        fileHandler.save(imageBytes, imageId);

        return new BabyAlbumUploadResponse(imageId);
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

        String age = babyFacade.findBabyDetails(dto.getClientId())
                .map(BabyResponse::getDateOfBirth)
                .map(dateOfBirth -> {
                    LocalDate now = LocalDate.now();

                    Period period = Period.between(dateOfBirth, now);

                    return String.format("%s years and %s months", period.getYears(), period.getMonths());
                }).orElse(null);

        return BabyAlbumResponse.builder()
                .id(dto.getId())
                .kidAge(age)
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
                        .url(getPathToGetImageEncodedMethod(dto.getId(), dto.getClientId()))
                        .build())
                .collect(Collectors.toList());
    }

    private String getPathToGetImageEncodedMethod(String id, String clientId) {
        ControllerLinkBuilder linkBuilder =
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder.methodOn(BabyAlbumController.class)
                                .getImageEncoded(id, clientId));

        URI uri = linkBuilder.toUri();
        return uri.getPath();
    }

    @Transactional
    public boolean deleteImage(String imageId, String clientId) {

        boolean deleted = repository.deleteImageEntry(imageId, clientId);

        if (deleted) {
            fileHandler.delete(imageId);
        }

        return deleted;
    }
}
