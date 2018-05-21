package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;

import java.util.Set;

public interface RestaurantService {

    Restaurant getById(Long id);

    Restaurant createRestaurant(String name, String city, String street, String phoneNumber, Restorer restorer, Set<Tag> tags);

    void updateRestaurant(Restaurant restaurant);

    void updateRestaurantTags(Long restaurantId, Set<Tag> tags);

}
