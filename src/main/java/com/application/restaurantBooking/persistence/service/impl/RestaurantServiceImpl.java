package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.repository.RestaurantRepository;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public Restaurant createRestaurant(String name, String city, String street, Restorer restorer) {
        return createRestaurant(name, city, street, restorer, Collections.emptySet());
    }

    @Override
    public Restaurant createRestaurant(String name, String city, String street, Restorer restorer, Set<Tag> tags) {
        Restaurant restaurant = new RestaurantBuilder()
                .name(name).city(city).street(street).restorer(restorer).tags(tags).build();
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
