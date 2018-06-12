package com.application.restaurantBooking;

import com.application.restaurantBooking.persistence.builder.*;
import com.application.restaurantBooking.persistence.model.*;
import com.application.restaurantBooking.persistence.service.ReservationService;
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

    private ReservationService reservationService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DatabaseInitializer(RestorerService restorerService,
                               RestaurantService restaurantService,
                               RestaurantTableService restaurantTableService,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               ReservationService reservationService) {
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.reservationService = reservationService;
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        Map<DayOfWeek, OpenHours> map = new HashMap<>();
        try {
            Reservation reservation = new ReservationBuilder()
                    .restaurantTable(restaurantTable)
                    .reservationDate(sdf.parse("2018-06-10_12:30"))
                    .reservationLength(2)
                    .reservedPlaces(10)
                    .comment("comments...")
                    .build();
            reservationService.createReservation(reservation);
            reservation = new ReservationBuilder()
                    .restaurantTable(restaurantTable)
                    .reservationDate(sdf.parse("2018-06-15_22:30"))
                    .reservationLength(1)
                    .reservedPlaces(5)
                    .comment("comments...")
                    .build();
            reservationService.createReservation(reservation);
            reservation = new ReservationBuilder()
                    .restaurantTable(restaurantTable)
                    .reservationDate(sdf.parse("2018-06-5_21:00"))
                    .reservationLength(3)
                    .reservedPlaces(7)
                    .comment("comments...")
                    .build();
            reservationService.createReservation(reservation);

            OpenHours day1 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day2 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day3 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();

            map.put(DayOfWeek.MONDAY, day1);
            map.put(DayOfWeek.THURSDAY, day2);
            map.put(DayOfWeek.WEDNESDAY, day3);
            restaurantService.addOpenHours(restaurant, map);

            OpenHours day4 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("20:30")).build();
            OpenHours day5 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("19:30")).build();
            map.put(DayOfWeek.SATURDAY, day4);
            map.put(DayOfWeek.SUNDAY, day5);
            restaurantService.addOpenHours(restaurant, map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
