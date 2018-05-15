package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @RequestMapping(value = UrlRequests.GET_ALL_RESTAURANTS,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getAllRestaurants() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(restaurantService.getAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    @RequestMapping(value = UrlRequests.GET_RESTAURANT_BY_ID,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getTaskById(@PathVariable String id){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(restaurantService.getById(Long.decode(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

}
