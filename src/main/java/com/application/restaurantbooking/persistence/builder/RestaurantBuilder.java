package com.application.restaurantbooking.persistence.builder;

import com.application.restaurantbooking.persistence.model.*;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

public class RestaurantBuilder {

    private Restaurant restaurant;

    public RestaurantBuilder() {
        restaurant = new Restaurant();
    }

    public RestaurantBuilder name(String name) {
        restaurant.setName(StringUtils.stripAccents(name));
        return this;
    }

    public RestaurantBuilder city(String city) {
        restaurant.setCity(StringUtils.stripAccents(city));
        return this;
    }

    public RestaurantBuilder street(String street) {
        restaurant.setStreet(StringUtils.stripAccents(street));
        return this;
    }

    public RestaurantBuilder streetNumber(String streetNumber) {
        restaurant.setStreetNumber(streetNumber);
        return this;
    }

    public RestaurantBuilder phoneNumber(String phoneNumber) {
        restaurant.setPhoneNumber(phoneNumber);
        return this;
    }

    public RestaurantBuilder website(String website) {
        restaurant.setWebsite(website);
        return this;
    }

    public RestaurantBuilder price(Price price) {
        restaurant.setPrice(price);
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

    public RestaurantBuilder openHours(Map<DayOfWeek, OpenHours> openHoursMap) {
        restaurant.setOpenHoursMap(openHoursMap);
        return this;
    }

    public RestaurantBuilder localization(Double lat, Double lon) {
        restaurant.setLatitude(lat);
        restaurant.setLongitude(lon);
        return this;
    }

    public Restaurant build() {
        return restaurant;
    }
}
