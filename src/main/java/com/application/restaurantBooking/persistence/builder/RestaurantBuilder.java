package com.application.restaurantBooking.persistence.builder;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;

import java.util.Set;

public class RestaurantBuilder {

    private Restaurant restaurant;

    public RestaurantBuilder() {
        restaurant = new Restaurant();
    }

    public RestaurantBuilder name(String name) {
        restaurant.setName(name);
        return this;
    }

    public RestaurantBuilder city(String city) {
        restaurant.setCity(city);
        return this;
    }

    public RestaurantBuilder street(String street) {
        restaurant.setStreet(street);
        return this;
    }

    public RestaurantBuilder restorer(Restorer restorer) {
        restaurant.setRestorer(restorer);
        return this;
    }

    public RestaurantBuilder tags(Set<Tag> tags) {
        restaurant.setTags(tags);
        return this;
    }

    public Restaurant build() {
        return restaurant;
    }
}
