package com.application.restaurantBooking.persistence.builder;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;

public class RestaurantBuilder {

    private Restaurant restaurant;

    public RestaurantBuilder() {
        restaurant = new Restaurant();
    }

    public RestaurantBuilder name(String name) {
        restaurant.setName(name);
        return this;
    }

    public RestaurantBuilder address(String address) {
        restaurant.setAddress(address);
        return this;
    }

    public RestaurantBuilder restorer(Restorer restorer) {
        restaurant.setRestorer(restorer);
        return this;
    }

    public RestaurantBuilder tags(String tags) {
        restaurant.setTags(tags);
        return this;
    }

    public Restaurant build() {
        return restaurant;
    }
}
