package com.application.restaurantBooking;

import com.application.restaurantBooking.persistence.builder.OpenHoursBuilder;
import com.application.restaurantBooking.persistence.model.OpenHours;
import com.application.restaurantBooking.persistence.model.Restaurant;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.model.Tag;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        Map<DayOfWeek, OpenHours> map = new HashMap<>();
        try {
            OpenHours day1 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day2 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day3 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();

            map.put(DayOfWeek.MONDAY, day1);
            map.put(DayOfWeek.THURSDAY, day2);
            map.put(DayOfWeek.WEDNESDAY, day3);
            restaurantService.updateOpenHours(restaurant.getId(), map);

            OpenHours day4 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("20:30")).build();
            OpenHours day5 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("19:30")).build();
            map.put(DayOfWeek.MONDAY, day4);
            map.put(DayOfWeek.SUNDAY, day5);
            restaurantService.updateOpenHours(restaurant.getId(), map);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
