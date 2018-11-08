package com.application.restaurantbooking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {

    private Long id;

    private String username;

    private boolean enabled;

}
