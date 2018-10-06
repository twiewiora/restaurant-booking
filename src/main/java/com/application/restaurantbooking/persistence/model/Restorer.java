package com.application.restaurantbooking.persistence.model;

import javax.persistence.*;

@Entity
@Table(name = "restorer")
public class Restorer extends User {

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "restorer", orphanRemoval = true)
    private Restaurant restaurant;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
