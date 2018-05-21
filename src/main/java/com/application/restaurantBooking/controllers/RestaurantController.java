package com.application.restaurantBooking.controllers;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
public class RestaurantController {

    private RestaurantService restaurantService;

    private RestorerService restorerService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, RestorerService restorerService) {
        this.restaurantService = restaurantService;
        this.restorerService = restorerService;
    }

    @RequestMapping(value = UrlRequests.POST_RESTAURANT_ADD,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8")
    public void createRestaurant(@RequestBody String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            Restorer restorer = restorerService.getById(jsonNode.get("restorerId").asLong());
            if (restorer != null) {
                JsonNode tagsNode = jsonNode.get("tags");
                Set<Tag> tags = new HashSet<>();
                if (tagsNode.isArray()) {
                    for (JsonNode tag : tagsNode) {
                        tags.add(Tag.valueOf(tag.asText().toUpperCase()));
                    }
                }
                restaurantService.createRestaurant(jsonNode.get("name").asText(), jsonNode.get("city").asText(),
                        jsonNode.get("street").asText(), jsonNode.get("phoneNumber").asText(), restorer, tags);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = UrlRequests.POST_RESTAURANT_UPDATE,
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8")
    public void updateRestaurant(@RequestBody String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            Restaurant restaurant = restaurantService.getById(jsonNode.get("restaurantId").asLong());
            if (restaurant != null) {
                restaurant.setName(jsonNode.get("name").asText());
                restaurant.setCity(jsonNode.get("city").asText());
                restaurant.setStreet(jsonNode.get("street").asText());
                restaurant.setPhoneNumber(jsonNode.get("phoneNumber").asText());
                JsonNode tagsNode = jsonNode.get("tags");
                Set<Tag> tags = new HashSet<>();
                if (tagsNode.isArray()) {
                    for (JsonNode tag : tagsNode) {
                        tags.add(Tag.valueOf(tag.asText().toUpperCase()));
                    }
                }
                restaurant.setTags(tags);
                restaurantService.updateRestaurant(restaurant);
                restaurantService.updateRestaurantTags(restaurant.getId(), tags);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = UrlRequests.GET_OPEN_HOURS_ALL,
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public String getOpenHoursForAllDays(@PathVariable String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Restaurant restaurant = restaurantService.getById(Long.decode(id));
            if (restaurant != null) {
                return objectMapper.writeValueAsString(restaurant.getOpenHoursMap());
            } else {
                return UrlRequests.ERROR;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return UrlRequests.ERROR;
        }
    }

}
