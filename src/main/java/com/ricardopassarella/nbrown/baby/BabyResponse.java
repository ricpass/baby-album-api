package com.ricardopassarella.nbrown.baby;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
class BabyResponse {

    private final String name;
    private final String gender;
    private final LocalDate dateOfBirth;
}
