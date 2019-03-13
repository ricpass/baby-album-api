package com.ricardopassarella.nbrown.client;


import com.ricardopassarella.nbrown.client.dto.BabyClient;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ClientFacade {

    private final ClientRepository repository;

    public Optional<BabyClient> findById(String clientId) {
        return repository.findById(clientId);
    }

}
