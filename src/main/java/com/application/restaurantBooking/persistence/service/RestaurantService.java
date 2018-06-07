package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.OpenHours;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Tag;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

public interface RestaurantService {

    Restaurant getById(Long id);

    Restaurant createRestaurant(Restaurant restaurant);

    void updateRestaurant(Restaurant restaurant);

    void updateRestaurantTags(Long restaurantId, Set<Tag> tags);

    void addOpenHours(Long restaurantId, Map<DayOfWeek, OpenHours> openHoursMap);

}
