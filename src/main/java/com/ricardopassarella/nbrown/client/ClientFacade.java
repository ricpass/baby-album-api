package com.ricardopassarella.nbrown.client;


import com.ricardopassarella.nbrown.client.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientFacade {

    private final ClientRepository repository;

    public Optional<ClientDto> findById(String clientId) {
        return repository.findById(clientId);
    }

}
