package com.ricardopassarella.nbrown.babyalbum;

import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumImageRequest;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumResponse;
import com.ricardopassarella.nbrown.babyalbum.dto.BabyAlbumResponseWithLinks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/baby/album")
@RequiredArgsConstructor
class BabyAlbumController {

    private final BabyAlbumService service;

    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestHeader(value = "Client-Id") String clientId) {

        // TODO request was rejected
        //  because its size (16862916) exceeds the configured maximum (10485760)

        service.processImage(multipartFile, clientId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/base64")
    public ResponseEntity<String> handleFileUploadAsBase64(@RequestBody BabyAlbumImageRequest imageRequest,
                                                           @RequestHeader(value = "Client-Id") String clientId) {

        service.processImage(imageRequest.getImageBase64(), clientId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") String imageId,
                                           @RequestHeader(value = "Client-Id") String clientId) {

        return service.getImage(imageId, clientId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<BabyAlbumResponseWithLinks>> getImagesLinks(@RequestHeader(value = "Client-Id") String clientId) {

        List<BabyAlbumResponseWithLinks> response = service.getImagesAsLinks(clientId);

        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/base64/{imageId}")
    public ResponseEntity<BabyAlbumResponse> getImageEncoded(@PathVariable("imageId") String imageId,
                                                             @RequestHeader(value = "Client-Id") String clientId) {
        return service.getImageAsBase64(imageId, clientId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/base64")
    public ResponseEntity<List<BabyAlbumResponse>> getImagesAsBase64(@RequestHeader(value = "Client-Id") String clientId) {


        List<BabyAlbumResponse> response = service.getImagesAsBase64(clientId);

        return ResponseEntity.ok(response);
    }

}
