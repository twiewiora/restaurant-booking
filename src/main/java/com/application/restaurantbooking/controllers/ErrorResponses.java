package com.application.restaurantbooking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorResponses {

    private ErrorResponses(){
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static final String RESTAURANT_NOT_FOUND = mapper.
            createObjectNode().put("desc", "Restaurant was not found").toString();

    public static final String RESTAURANT_TABLE_NOT_FOUND = mapper
            .createObjectNode().put("desc", "Restaurant table was not found").toString();

    public static final String RESTAURANT_TABLE_RESERVED = mapper
            .createObjectNode().put("desc", "Restaurant table reserved").toString();

    public static final String RESERVATION_NOT_FOUND = mapper
            .createObjectNode().put("desc", "Reservation was not found").toString();

    public static final String UNAUTHORIZED_ACCESS = mapper
            .createObjectNode().put("desc", "Unauthorized access attempt").toString();

    public static final String CLIENT_NOT_FOUND = mapper
            .createObjectNode().put("desc", "Client was not found").toString();

    public static final String INTERNAL_ERROR = mapper
            .createObjectNode().put("desc", "Internal error!").toString();

    public static final String JWT_TOKEN_EXPIRED = mapper
            .createObjectNode().put("desc", "JWT token expired!").toString();

}
