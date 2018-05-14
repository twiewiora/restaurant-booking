package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.repository.RestaurantRepository;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        return new ArrayList<>(restaurantRepository.findAll());
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant createRestaurant(String name, String address, Restorer restorer) {
        return createRestaurant(name, address, restorer, "");
    }

    @Override
    public Restaurant createRestaurant(String name, String address, Restorer restorer, String tags) {
        Restaurant restaurant = new RestaurantBuilder()
                .name(name).address(address).restorer(restorer).tags(tags).build();
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public List<RestaurantTable> getFreeTables(Long restaurantID) {
        return restaurantRepository.getFreeTables(restaurantID);
    }
}
