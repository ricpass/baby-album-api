package com.ricardopassarella.nbrown.babyalbum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@RequiredArgsConstructor
public class BabyAlbumResponse {

    private final String id;
    private final String kidAge;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String fullAddress;
    private final LocalDateTime dateTime;
    private final String imageBase64;
}
