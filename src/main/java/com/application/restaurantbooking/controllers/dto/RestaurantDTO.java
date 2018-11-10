package com.application.restaurantbooking.controllers.dto;

import com.application.restaurantbooking.persistence.model.Price;
import com.application.restaurantbooking.persistence.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class RestaurantDTO {

    private Long id;

    private String name;

    private String city;

    private String street;

    private String streetNumber;

    private String phoneNumber;

    private String website;

    private Double longitude;

    private Double latitude;

    private Collection<Price> restaurantPrice;

    private Double priority;

    private Collection<Tag> tags = new HashSet<>();

}
