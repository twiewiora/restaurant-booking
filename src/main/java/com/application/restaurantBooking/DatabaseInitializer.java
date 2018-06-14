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
        Restorer restorer = createRestorer("test", "test1");
        Restaurant restaurant = createRestaurant(restorer);

        RestaurantTable table1 = createRestaurantTable(restaurant, 12);
        RestaurantTable table2 = createRestaurantTable(restaurant, 5);
        RestaurantTable table3 = createRestaurantTable(restaurant, 2);
        RestaurantTable table4 = createRestaurantTable(restaurant, 7);
        RestaurantTable table5 = createRestaurantTable(restaurant, 4);
        RestaurantTable table6 = createRestaurantTable(restaurant, 3);

        createReservation(table1, "2018-06-15_22:30", 1);
        createReservation(table2, "2018-06-15_20:00", 3);
        createReservation(table3, "2018-06-15_19:00", 2);
        createReservation(table4, "2018-06-15_18:00", 1);
        createReservation(table5, "2018-06-15_17:00", 3);
        createReservation(table6, "2018-06-15_18:00", 2);

        createOpenHours(restaurant);
    }

    private Restorer createRestorer(String userName, String password) {
        Restorer restorer = new RestorerBuilder()
                .username(userName)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        return restorerService.createRestorer(restorer);
    }

    private Restaurant createRestaurant(Restorer restorer) {
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
        return restaurantService.createRestaurant(restaurant);
    }

    private RestaurantTable createRestaurantTable(Restaurant restaurant, Integer places) {
        RestaurantTable restaurantTable = new RestaurantTableBuilder()
                .restaurant(restaurant)
                .maxPlaces(places)
                .build();
        return restaurantTableService.createRestaurantTable(restaurantTable);
    }

    private Reservation createReservation(RestaurantTable restaurantTable, String date, Integer length) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            Reservation reservation = new ReservationBuilder()
                    .restaurantTable(restaurantTable)
                    .reservationDate(sdf.parse(date))
                    .reservationLength(length)
                    .reservedPlaces(10)
                    .comment("comments...")
                    .build();
            return reservationService.createReservation(reservation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createOpenHours(Restaurant restaurant) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            Map<DayOfWeek, OpenHours> map = new HashMap<>();
            OpenHours day1 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("23:30")).build();
            OpenHours day2 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day3 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("22:30")).build();
            OpenHours day4 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("20:30")).build();
            OpenHours day5 = new OpenHoursBuilder().openHour(f.parse("12:30")).closeHour(f.parse("19:30")).build();

            map.put(DayOfWeek.FRIDAY, day1);
            map.put(DayOfWeek.THURSDAY, day2);
            map.put(DayOfWeek.WEDNESDAY, day3);
            map.put(DayOfWeek.SATURDAY, day4);
            map.put(DayOfWeek.SUNDAY, day5);
            restaurantService.addOpenHours(restaurant, map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
