package com.application.restaurantBooking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorResponses {

    private static ObjectMapper mapper = new ObjectMapper();

    public static final String RESTAURANT_NOT_FOUND = mapper.
            createObjectNode().put("desc", "Restaurant was not found").toString();

    public static final String RESTAURANT_TABLE_NOT_FOUND = mapper
            .createObjectNode().put("desc", "Restaurant table was not found").toString();

    public static final String RESERVATION_NOT_FOUND = mapper
            .createObjectNode().put("desc", "Reservation was not found").toString();

    public static final String UNAUTHORIZED_ACCESS = mapper
            .createObjectNode().put("desc", "Unauthorized access attempt").toString();

    public static final String INTERNAL_ERROR = mapper
            .createObjectNode().put("desc", "Internal error!").toString();

}
