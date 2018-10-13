package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Price;
import com.application.restaurantbooking.persistence.model.Tag;

public interface ClientService {

    Client getById(Long id);

    Client getByUsername(String username);

    Client createClient(Client client);

    void deleteClient(Long id);

    void updateClient(Client client);

    Integer getStatisticsForPrice(Long clientId, Price price);

    Integer getStatisticsForTag(Long clientId, Tag tag);

    Integer getStatisticsForRestaurant(Long clientId, Long restaurantId);

    Integer getAmountClientReservations(Long clientId);

}
