package com.ricardopassarella.nbrown.baby;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BabyService {


    public Optional<BabyResponse> getBabyDetails(String clientId) {

        return Optional.empty();
    }

    public void updateBabyDetails(String clientId, BabyRequest babyRequest) {

    }
}
