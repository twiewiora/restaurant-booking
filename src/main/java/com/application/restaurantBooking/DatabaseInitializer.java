package com.application.restaurantBooking;

import com.application.restaurantBooking.persistence.builder.OpenHoursBuilder;
import com.application.restaurantBooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantBooking.persistence.builder.RestaurantTableBuilder;
import com.application.restaurantBooking.persistence.builder.RestorerBuilder;
import com.application.restaurantBooking.persistence.model.*;
import com.application.restaurantBooking.persistence.service.RestaurantService;
import com.application.restaurantBooking.persistence.service.RestaurantTableService;
import com.application.restaurantBooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DatabaseInitializer(RestorerService restorerService,
                               RestaurantService restaurantService,
                               RestaurantTableService restaurantTableService,
                               BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void initializeDatabase() {
        Restorer restorer = new RestorerBuilder()
                .username("test")
                .password(bCryptPasswordEncoder.encode("test1"))
                .build();
        restorerService.createRestorer(restorer);
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.KEBAB);
        tags.add(Tag.PIZZA);
        Restaurant restaurant = new RestaurantBuilder()
                .name("name")
                .city("city")
                .street("street")
                .phoneNumber("")
                .restorer(restorer)
                .tags(tags)
                .build();
        restaurantService.createRestaurant(restaurant);

        RestaurantTable restaurantTable = new RestaurantTableBuilder()
                .restaurant(restaurant)
                .maxPlaces(12)
                .build();
        restaurantTableService.createRestaurantTable(restaurantTable);
        restaurantTable = new RestaurantTableBuilder()
                .restaurant(restaurant)
                .maxPlaces(5)
                .build();
        restaurantTableService.createRestaurantTable(restaurantTable);
        restaurantTable = new RestaurantTableBuilder()
                .restaurant(restaurant)
                .maxPlaces(2)
                .build();
        restaurantTableService.createRestaurantTable(restaurantTable);

        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        Map<DayOfWeek, OpenHours> map = new HashMap<>();
        try {
            OpenHours day1 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day2 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day3 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();

            map.put(DayOfWeek.MONDAY, day1);
            map.put(DayOfWeek.THURSDAY, day2);
            map.put(DayOfWeek.WEDNESDAY, day3);
            restaurantService.addOpenHours(restaurant.getId(), map);

            OpenHours day4 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("20:30")).build();
            OpenHours day5 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("19:30")).build();
            map.put(DayOfWeek.MONDAY, day4);
            map.put(DayOfWeek.SUNDAY, day5);
            restaurantService.addOpenHours(restaurant.getId(), map);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
