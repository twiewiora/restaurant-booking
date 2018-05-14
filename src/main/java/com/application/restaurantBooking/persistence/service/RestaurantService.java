package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.model.Restorer;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAll();

    Restaurant getById(Long id);

    Restaurant createRestaurant(String name, String address, Restorer restorer);

    Restaurant createRestaurant(String name, String address, Restorer restorer, String tags);

    void deleteRestaurant(Long id);

    List<RestaurantTable> getFreeTables(Long restaurantID);

}
