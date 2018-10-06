package com.application.restaurantbooking.utils;

import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.utils.geocoding.Localization;

import java.util.Set;

public class RestaurantSearcherRequest {

    private Localization localization;

    private Integer radius;

    private Set<Tag> tags;

    public RestaurantSearcherRequest(Localization localization, Integer radius, Set<Tag> tags) {
        this.localization = localization;
        this.radius = radius;
        this.tags = tags;
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
}
