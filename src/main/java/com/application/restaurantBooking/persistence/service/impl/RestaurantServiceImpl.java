package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.model.OpenHours;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.repository.OpenHoursRepository;
import com.application.restaurantBooking.persistence.repository.RestaurantRepository;
import com.application.restaurantBooking.persistence.service.RestaurantService;
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
    public Restaurant createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.updateRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getCity(),
                restaurant.getStreet(), restaurant.getPhoneNumber());
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
    public void deleteOpenHours(Restaurant restaurant) {
        List<OpenHours> oldOpenHours = new ArrayList<>(restaurant.getOpenHoursMap().values());
        restaurant.getOpenHoursMap().clear();
        restaurantRepository.save(restaurant);
        oldOpenHours.forEach(openHours -> openHoursRepository.delete(openHours));
    }
}
