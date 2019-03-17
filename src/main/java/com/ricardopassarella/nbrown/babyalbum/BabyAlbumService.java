package com.ricardopassarella.nbrown.babyalbum;

import com.ricardopassarella.nbrown.baby.BabyFacade;
import com.ricardopassarella.nbrown.baby.dto.BabyResponse;
import com.ricardopassarella.nbrown.babyalbum.dto.*;
import com.ricardopassarella.nbrown.common.PageableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
class BabyAlbumService {

    private final BabyAlbumRepository repository;
    private final MetadataExtractor metadataExtractor;
    private final BabyFileHandler fileHandler;
    private final BabyFacade babyFacade;

    private final int pageLimit;

    public BabyAlbumService(BabyAlbumRepository repository,
                            MetadataExtractor metadataExtractor,
                            BabyFileHandler fileHandler,
                            BabyFacade babyFacade,
                            @Value("${baby-album.get-images.page.limit}") int pageLimit) {
        this.repository = repository;
        this.metadataExtractor = metadataExtractor;
        this.fileHandler = fileHandler;
        this.babyFacade = babyFacade;
        this.pageLimit = pageLimit;
    }

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

    PageableResponse<List<BabyAlbumResponse>> getImagesAsBase64(String clientId, int page) {
        int offset = page * pageLimit;

        List<BabyAlbumResponse> response = repository.findImagesByClientId(clientId, pageLimit, offset)
                .stream()
                .map(this::createBabyAlbumResponse)
                .collect(Collectors.toList());

        Integer count = repository.getTotalCountOfImage(clientId);

        return new PageableResponse<>(response, count);
    }

    private BabyAlbumResponse createBabyAlbumResponse(BabyAlbumDto dto) {
        byte[] imageBytes = fileHandler.readImageFromFile(dto.getId());
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        String age = getKidAge(dto.getClientId());

        return BabyAlbumResponse.builder()
                .id(dto.getId())
                .kidAge(age)
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .dateTime(dto.getDateTime())
                .imageBase64(encodedImage)
                .build();
    }

    private String getKidAge(String clientId) {
        return babyFacade.findBabyDetails(clientId)
                .map(BabyResponse::getDateOfBirth)
                .map(dateOfBirth -> {
                    LocalDate now = LocalDate.now();

                    Period period = Period.between(dateOfBirth, now);

                    return String.format("%s years and %s months", period.getYears(), period.getMonths());
                }).orElse(null);
    }

    PageableResponse<List<BabyAlbumResponseWithLinks>> getImagesAsLinks(String clientId, int page) {
        int offset = page * pageLimit;

        String kidAge = getKidAge(clientId);

        List<BabyAlbumResponseWithLinks> response = repository.findImagesByClientId(clientId, pageLimit, offset)
                .stream()
                .map(dto -> BabyAlbumResponseWithLinks.builder()
                        .id(dto.getId())
                        .kidAge(kidAge)
                        .latitude(dto.getLatitude())
                        .longitude(dto.getLongitude())
                        .dateTime(dto.getDateTime())
                        .url(getPathToGetImageEncodedMethod(dto.getId(), dto.getClientId()))
                        .build())
                .collect(Collectors.toList());

        Integer count = repository.getTotalCountOfImage(clientId);

        return new PageableResponse<>(response, count);
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
