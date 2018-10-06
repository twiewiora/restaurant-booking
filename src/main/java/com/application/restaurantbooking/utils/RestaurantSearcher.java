package com.application.restaurantbooking.utils;

import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.utils.geocoding.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantSearcher {

    @Autowired
    public RestaurantSearcher() {
    }

    public List<Restaurant> getSurroundingRestaurant(List<Restaurant> restaurantsInCity,
                                                     RestaurantSearcherRequest request) {
        List<Restaurant> restaurantsInRange = restaurantsInCity.stream()
                .filter(restaurant -> isRestaurantInRange(restaurant, request))
                .filter(restaurant -> restaurant.getTags().containsAll(request.getTags()))
                .collect(Collectors.toList());
        // sort

        return restaurantsInRange;
    }

    private boolean isRestaurantInRange(Restaurant restaurant, RestaurantSearcherRequest request) {
        return getDistanceToRestaurant(request.getLocalization(),
                new Localization(restaurant.getLatitude(), restaurant.getLongitude())) < request.getRadius();
    }

    private Double getDistanceToRestaurant(Localization clientLocalization, Localization restaurantLocalization) {
        return Math.sqrt(Math.pow(clientLocalization.getLatitude() - restaurantLocalization.getLatitude(), 2) +
                Math.pow(Math.cos((restaurantLocalization.getLatitude() * Math.PI) / 180) *
                        (clientLocalization.getLongitude() - restaurantLocalization.getLongitude()), 2)) *
                (40075.704 / 360) * 1000;
    }
}
