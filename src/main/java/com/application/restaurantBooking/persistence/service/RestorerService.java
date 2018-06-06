package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restorer;

public interface RestorerService {

    Restorer getByUsername(String username);

    Restorer createRestorer(Restorer restorer);

    Restorer createRestorer(String login, String password);

}
