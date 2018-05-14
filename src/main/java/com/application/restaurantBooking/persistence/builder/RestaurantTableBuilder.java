package com.application.restaurantBooking.persistence.builder;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;

public class RestaurantTableBuilder {

    private RestaurantTable restaurantTable;

    public RestaurantTableBuilder() {
        restaurantTable = new RestaurantTable();
    }

    public RestaurantTableBuilder restaurant(Restaurant restaurant) {
        restaurantTable.setRestaurant(restaurant);
        return this;
    }

    public RestaurantTableBuilder maxPlaces(Integer maxPlaces) {
        restaurantTable.setMaxPlaces(maxPlaces);
        return this;
    }

    public RestaurantTableBuilder reservedPlaces(Integer reservedPlaces) {
        restaurantTable.setReservedPlaces(reservedPlaces);
        return this;
    }

    public RestaurantTable build() {
        return restaurantTable;
    }
}
