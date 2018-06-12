package com.application.restaurantBooking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AcceptResponses {

    private static ObjectMapper mapper = new ObjectMapper();

    public static final String RESTAURANT_CREATED = mapper
            .createObjectNode().put("desc", "Restaurant created").toString();

    public static final String RESTAURANT_UPDATED = mapper
            .createObjectNode().put("desc", "Restaurant updated").toString();

    public static final String OPEN_HOURS_UPDATED = mapper
            .createObjectNode().put("desc", "Open hours updated").toString();

    public static final String RESTAURANT_TABLE_CREATED = mapper
            .createObjectNode().put("desc", "Restaurant table was created").toString();

    public static final String RESTAURANT_TABLE_UPDATED = mapper
            .createObjectNode().put("desc", "Restaurant table was updated").toString();

    public static final String RESTAURANT_TABLE_DELETED = mapper
            .createObjectNode().put("desc", "Restaurant table was deleted").toString();

    public static final String RESERVATION_CREATED = mapper
            .createObjectNode().put("desc", "Reservation created").toString();

    public static final String RESERVATION_DELETED = mapper
            .createObjectNode().put("desc", "Reservation deleted").toString();

}