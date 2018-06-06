package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.model.Client;
import com.application.restaurantBooking.persistence.repository.ClientRepository;
import com.application.restaurantBooking.persistence.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client createClient(Client client) {
        clientRepository.save(client);
        return client;
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
