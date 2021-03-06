package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.OpenHours;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.Tag;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RestaurantService {

    Restaurant getById(Long id);

    List<Restaurant> getAll();

    List<Restaurant> getRestaurantByNameAndCity(String name, String city);

    Restaurant createRestaurant(Restaurant restaurant);

    void updateRestaurant(Restaurant restaurant);

    void updateRestaurantTags(Restaurant restaurant, Set<Tag> tags);

    void addOpenHours(Restaurant restaurant, Map<DayOfWeek, OpenHours> openHoursMap);

    void updateOpenHours(Restaurant restaurant);

}
