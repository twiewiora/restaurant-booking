package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.Restorer;

public interface RestorerService {

    Restorer getByUsername(String username);

    Restorer createRestorer(Restorer restorer);

}
