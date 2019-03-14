package com.ricardopassarella.nbrown.client;


import com.ricardopassarella.nbrown.client.dto.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientRepository repository;

    public Optional<ClientResponse> findById(String clientId) {
        return repository.findById(clientId);
    }

}
