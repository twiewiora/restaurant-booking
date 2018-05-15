package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;

import java.util.List;
import java.util.Set;

public interface RestaurantService {

    List<Restaurant> getAll();

    Restaurant getById(Long id);

    Restaurant createRestaurant(String name, String city, String street, Restorer restorer);

    Restaurant createRestaurant(String name, String city, String street, Restorer restorer, Set<Tag> tags);

    void deleteRestaurant(Long id);

    List<RestaurantTable> getFreeTables(Long restaurantID);

}
