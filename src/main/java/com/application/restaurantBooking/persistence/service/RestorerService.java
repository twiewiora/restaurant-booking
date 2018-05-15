package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restorer;

public interface RestorerService {

    Restorer getById(Long id);

    Restorer createRestorer(String login, String password);

    void deleteRestorer(Long id);

}
