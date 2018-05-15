package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.builder.RestaurantTableBuilder;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.repository.RestaurantTableRepository;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public RestaurantTable getById(Long id) {
        return restaurantTableRepository.findById(id).orElse(null);
    }

    @Override
    public RestaurantTable createRestaurantTable(Restaurant restaurant, Integer maxPlaces) {
        RestaurantTable restaurantTable = new RestaurantTableBuilder()
                .restaurant(restaurant).maxPlaces(maxPlaces).build();
        restaurantTableRepository.save(restaurantTable);
        return restaurantTable;
    }

    @Override
    public void deleteRestaurantTable(Long id) {
        restaurantTableRepository.deleteById(id);
    }
}
