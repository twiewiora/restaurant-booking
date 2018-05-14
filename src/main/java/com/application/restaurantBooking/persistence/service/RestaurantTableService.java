package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;

public interface RestaurantTableService {

    RestaurantTable getById(Long id);

    RestaurantTable createRestaurantTable(Restaurant restaurant, Integer maxPlaces);

    void deleteRestaurantTable(Long id);

}
