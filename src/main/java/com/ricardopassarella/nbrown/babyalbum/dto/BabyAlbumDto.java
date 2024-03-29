package com.ricardopassarella.nbrown.babyalbum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class BabyAlbumDto {

    private final String id;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String fullAddress;
    private final LocalDateTime dateTime;
    private final String clientId;
}
