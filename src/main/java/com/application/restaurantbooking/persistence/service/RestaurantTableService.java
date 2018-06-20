package com.application.restaurantbooking.persistence.service;

import com.application.restaurantbooking.persistence.model.RestaurantTable;

public interface RestaurantTableService {

    RestaurantTable getById(Long id);

    RestaurantTable createRestaurantTable(RestaurantTable restaurantTable);

    void deleteRestaurantTable(Long id);

    void updateRestaurantTable(RestaurantTable restaurantTable);

}
