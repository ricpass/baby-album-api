package com.ricardopassarella.nbrown.babyalbum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/baby/album")
@RequiredArgsConstructor
class BabyPictureController {

    @GetMapping
    ResponseEntity<String> get(@RequestHeader(value = "Client-Id") String clientId) {

        return new ResponseEntity<>(clientId, OK);
    }

}
