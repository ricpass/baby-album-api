package com.ricardopassarella.nbrown.baby;

import com.ricardopassarella.nbrown.baby.dto.BabyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BabyFacade {

    private final BabyService service;

    public Optional<BabyResponse> findBabyDetails(String clientId) {
        return service.getBabyDetails(clientId);
    }

}
