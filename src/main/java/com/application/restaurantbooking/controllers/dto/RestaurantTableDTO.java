package com.application.restaurantbooking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantTableDTO {

    private Long id;

    private Integer maxPlaces;

    private Boolean reserved;

    private String comment;

    private String identifier;

}
