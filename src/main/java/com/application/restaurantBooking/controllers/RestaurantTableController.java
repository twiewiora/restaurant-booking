package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.RestaurantTable;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RestaurantTableController {

    private RestaurantService restaurantService;

    private RestaurantTableService restaurantTableService;

    @Autowired
    public RestaurantTableController(RestaurantService restaurantService,
                                     RestaurantTableService restaurantTableService){
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
    }

    @RequestMapping(value = UrlRequests.GET_TABLES_FOR_RESTAURANT,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getAllTablesForRestaurant(@PathVariable String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Restaurant restaurant = restaurantService.getById(Long.decode(id));
            if (restaurant != null) {
                return objectMapper.writeValueAsString(restaurant.getRestaurantTables());
            } else {
                return UrlRequests.ERROR;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return UrlRequests.ERROR;
        }
    }

    @RequestMapping(value = UrlRequests.POST_TABLE_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8")
    public void createRestaurantTable(@RequestBody String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode mainNode = objectMapper.readTree(json);
            Restaurant restaurant = restaurantService
                    .getById(Long.decode(mainNode.get("restaurantId").asText()));
            restaurantTableService.createRestaurantTable(restaurant, mainNode.get("maxPlaces").asInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = UrlRequests.DELETE_TABLE,
            method = RequestMethod.DELETE)
    public void deleteRestaurantTable(@PathVariable String id){
        RestaurantTable restaurantTable = restaurantTableService.getById(Long.decode(id));
        if (restaurantTable != null) {
            restaurantTableService.deleteRestaurantTable(Long.decode(id));
        }
    }

}
