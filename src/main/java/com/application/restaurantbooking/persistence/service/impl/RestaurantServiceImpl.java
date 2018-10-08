package com.application.restaurantbooking.persistence.service.impl;

import com.application.restaurantbooking.persistence.model.OpenHours;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.persistence.repository.OpenHoursRepository;
import com.application.restaurantbooking.persistence.repository.RestaurantRepository;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    private OpenHoursRepository openHoursRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 OpenHoursRepository openHoursRepository){
        this.restaurantRepository = restaurantRepository;
        this.openHoursRepository = openHoursRepository;
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> getRestaurantByCity(String city) {
        return restaurantRepository.findByCity(city);
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.updateRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getCity(),
                restaurant.getStreet(), restaurant.getStreetNumber(), restaurant.getPhoneNumber(), restaurant.getWebsite());
    }

    @Override
    public void updateRestaurantTags(Restaurant restaurant, Set<Tag> tags) {
        restaurant.setTags(tags);
    }

    @Override
    public void addOpenHours(Restaurant restaurant, Map<DayOfWeek, OpenHours> openHoursMap) {
        openHoursMap.values().forEach(openHours -> openHoursRepository.save(openHours));
        openHoursMap.forEach(restaurant.getOpenHoursMap()::put);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void updateOpenHours(Restaurant restaurant) {
        restaurant.getOpenHoursMap().values().forEach(openHours -> openHoursRepository.save(openHours));
        restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteOpenHours(Restaurant restaurant) {
        List<OpenHours> oldOpenHours = new ArrayList<>(restaurant.getOpenHoursMap().values());
        restaurant.getOpenHoursMap().clear();
        restaurantRepository.save(restaurant);
        oldOpenHours.forEach(openHours -> openHoursRepository.delete(openHours));
    }
}
