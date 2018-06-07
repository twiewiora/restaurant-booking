package com.application.restaurantBooking.persistence.service;

import com.application.restaurantBooking.persistence.model.RestaurantTable;

public interface RestaurantTableService {

    RestaurantTable getById(Long id);

    RestaurantTable createRestaurantTable(RestaurantTable restaurantTable);

    void deleteRestaurantTable(Long id);

    void updateRestaurantTable(RestaurantTable restaurantTable);

}
