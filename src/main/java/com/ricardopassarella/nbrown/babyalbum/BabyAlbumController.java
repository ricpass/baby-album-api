package com.ricardopassarella.nbrown.babyalbum;

import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumImageRequest;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumResponse;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumResponseWithLinks;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/baby/picture")
@RequiredArgsConstructor
class BabyAlbumController {

    private final BabyAlbumService service;

    @GetMapping(value = "/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId,
                                           @RequestHeader(value = "Client-Id") String clientId) {

        return service.getImage(imageId, clientId.toLowerCase())
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BabyAlbumUploadResponse> handleFileUpload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestHeader(value = "Client-Id") String clientId) {

        BabyAlbumUploadResponse response = service.processImage(multipartFile, clientId.toLowerCase());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BabyAlbumResponseWithLinks>> getImagesLinks(@RequestHeader(value = "Client-Id") String clientId) {

        List<BabyAlbumResponseWithLinks> response = service.getImagesAsLinks(clientId.toLowerCase());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/json/{imageId}")
    public ResponseEntity<BabyAlbumResponse> getImageEncoded(@PathVariable("imageId") String imageId,
                                                             @RequestHeader(value = "Client-Id") String clientId) {
        return service.getImageAsBase64(imageId, clientId.toLowerCase())
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/json")
    public ResponseEntity<BabyAlbumUploadResponse> handleFileUploadAsBase64(@RequestBody BabyAlbumImageRequest imageRequest,
                                                                            @RequestHeader(value = "Client-Id") String clientId) {

        BabyAlbumUploadResponse response =
                service.processImage(imageRequest.getImageBase64(), clientId.toLowerCase());

        return ResponseEntity.ok(response);
    }

    /*
     * This method can be really inefficient.
     * It is recommend to use BabyAlbumController.getImagesLinks instead to get all the links.
     * And download the images in multiple requests in paralel.
     */
    @GetMapping(value = "/json")
    public ResponseEntity<List<BabyAlbumResponse>> getImagesAsBase64(@RequestHeader(value = "Client-Id") String clientId) {


        List<BabyAlbumResponse> response = service.getImagesAsBase64(clientId.toLowerCase());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<Void> delete(@PathVariable("imageId") String imageId,
                                       @RequestHeader(value = "Client-Id") String clientId) {
        boolean deleted = service.deleteImage(imageId, clientId.toLowerCase());

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
