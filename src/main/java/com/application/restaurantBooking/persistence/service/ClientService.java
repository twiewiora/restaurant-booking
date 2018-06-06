package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Client;

public interface ClientService {

    Client getById(Long id);

    Client createClient(Client client);

    void deleteClient(Long id);

}
