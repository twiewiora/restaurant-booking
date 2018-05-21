package com.application.restaurantBooking;

import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer {

    private RestorerService restorerService;

    private RestaurantService restaurantService;

    private RestaurantTableService restaurantTableService;

    @Autowired
    public DatabaseInitializer(RestorerService restorerService,
                               RestaurantService restaurantService,
                               RestaurantTableService restaurantTableService) {
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
    }

    public void initializeDatabase() {
        Restorer restorer = restorerService.createRestorer("test", "test1");
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.KEBAB);
        tags.add(Tag.PIZZA);
        Restaurant restaurant = restaurantService.createRestaurant("name", "city", "street", "", restorer, tags);

        restaurantTableService.createRestaurantTable(restaurant, 12);
        restaurantTableService.createRestaurantTable(restaurant, 5);
        restaurantTableService.createRestaurantTable(restaurant, 2);
    }
}
