package com.ricardopassarella.nbrown.baby;

import com.ricardopassarella.nbrown.baby.dto.BabyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class BabyService {

    private final BabyRepository repository;

    Optional<BabyResponse> getBabyDetails(String clientId) {

        return repository.getBabyDetails(clientId);
    }

    void updateBabyDetails(String clientId, BabyRequest babyRequest) {
        boolean updated = repository.update(clientId, babyRequest);

        if (!updated){
            repository.insert(clientId, babyRequest);
        }
    }
}
