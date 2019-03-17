package com.ricardopassarella.nbrown.baby;

import com.ricardopassarella.nbrown.baby.dto.BabyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/baby")
@RequiredArgsConstructor
public class BabyController {

    private final BabyService service;

    @GetMapping
    ResponseEntity<BabyResponse> getBabyDetails(@RequestHeader(value = "Client-Id") String clientId) {
        return service.getBabyDetails(clientId.toLowerCase())
                .map(babyResponse -> new ResponseEntity<>(babyResponse, OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    ResponseEntity<Void> updateBabyDetails(@RequestHeader(value = "Client-Id") String clientId,
                                           @RequestBody BabyRequest babyRequest) {
        service.updateBabyDetails(clientId.toLowerCase(), babyRequest);

        return new ResponseEntity<>(NO_CONTENT);
    }


}
