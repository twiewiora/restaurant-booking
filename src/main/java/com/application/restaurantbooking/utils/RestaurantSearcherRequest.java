package com.application.restaurantbooking.utils;

import com.application.restaurantbooking.persistence.model.Price;
import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.utils.geocoding.Localization;

import java.util.Set;

public class RestaurantSearcherRequest {

    private Localization localization;

    private Integer radius;

    private Set<Tag> tags;

    private Set<Price> prices;

    private String restaurantName;

    public RestaurantSearcherRequest(Localization localization, Integer radius, Set<Tag> tags, Set<Price> prices,
                                     String restaurantName) {
        this.localization = localization;
        this.radius = radius;
        this.tags = tags;
        this.prices = prices;
        this.restaurantName = restaurantName;
    }

    public Localization getLocalization() {
        return localization;
    }

    public Integer getRadius() {
        return radius;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}
