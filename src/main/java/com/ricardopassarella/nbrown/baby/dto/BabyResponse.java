package com.ricardopassarella.nbrown.baby.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public class BabyResponse {

    private final String name;
    private final String gender;
    private final LocalDate dateOfBirth;
}
