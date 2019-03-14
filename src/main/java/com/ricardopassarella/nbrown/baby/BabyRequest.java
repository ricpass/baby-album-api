package com.ricardopassarella.nbrown.baby;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BabyRequest {

    private String name;
    private String gender;
    private LocalDate dateOfBirth;
}
