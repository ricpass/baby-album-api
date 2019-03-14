package com.ricardopassarella.nbrown.baby;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class BabyRequest {

    private final String name;
    private final String gender;
    private final LocalDate dateOfBirth;
}
