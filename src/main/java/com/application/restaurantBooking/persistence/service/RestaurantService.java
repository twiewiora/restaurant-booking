package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.OpenHours;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Tag;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RestaurantService {

    Restaurant getById(Long id);

    List<Restaurant> getAll();

    Restaurant createRestaurant(Restaurant restaurant);

    void updateRestaurant(Restaurant restaurant);

    void updateRestaurantTags(Restaurant restaurant, Set<Tag> tags);

    void addOpenHours(Restaurant restaurant, Map<DayOfWeek, OpenHours> openHoursMap);

    void deleteOpenHours(Restaurant restaurant);

}
