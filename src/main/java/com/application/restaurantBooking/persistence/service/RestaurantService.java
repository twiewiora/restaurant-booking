package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAll();

    Restaurant getById(Long id);

    Restaurant saveOrUpdate(Restaurant restaurant);

    void delete(Long id);

}
