package com.ricardopassarella.nbrown.baby;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
class BabyResponse {

    private final String name;
    private final String gender;
    private final LocalDate dateOfBirth;
}
