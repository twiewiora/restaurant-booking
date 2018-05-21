package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.repository.RestaurantRepository;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant createRestaurant(String name, String city, String street, String phoneNumber, Restorer restorer,
                                       Set<Tag> tags) {
        Restaurant restaurant = new RestaurantBuilder()
                .name(name).city(city).street(street).phoneNumber(phoneNumber).restorer(restorer)
                .tags(tags).build();
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.updateRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getCity(),
                restaurant.getStreet(), restaurant.getPhoneNumber());
    }

    @Override
    public void updateRestaurantTags(Long restaurantId, Set<Tag> tags) {
        Restaurant restaurant = getById(restaurantId);
        restaurant.setTags(tags);
    }
}
