package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.Client;

public interface ClientService {

    Client getById(Long id);

    Client getByUsername(String username);

    Client createClient(Client client);

    void deleteClient(Long id);

}
