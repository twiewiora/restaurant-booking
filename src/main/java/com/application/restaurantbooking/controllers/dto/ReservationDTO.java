package com.application.restaurantbooking.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDTO {

    private Long id;

    private Long clientId;

    private Integer reservedPlaces;

    private Integer reservationLength;

    private String comment;

    private Long restaurantTableId;

    private Long restaurantId;

    private String dateReservation;

    private boolean cancelled;

}
