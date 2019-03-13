package com.ricardopassarella.nbrown.domain.client;


import com.ricardopassarella.nbrown.domain.client.dto.Client;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ClientFacade {

    private final ClientRepository repository;

    public Optional<Client> findById(String clientId) {
        return repository.findById(clientId);
    }

}
