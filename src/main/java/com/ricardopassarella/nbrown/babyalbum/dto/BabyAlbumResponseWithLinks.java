package com.ricardopassarella.nbrown.babyalbum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class BabyAlbumResponseWithLinks {

    private final String id;
    private final String kidAge;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final LocalDateTime dateTime;
    private final String url;

}
