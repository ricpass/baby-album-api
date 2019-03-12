package com.ricardopassarella.nbrown.domain.kid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/baby")
@RequiredArgsConstructor
public class KidController {

    @GetMapping
    ResponseEntity<String> get(@RequestHeader(value = "Client-Id") String clientId) {



        return new ResponseEntity<>(clientId, OK);
    }

}
