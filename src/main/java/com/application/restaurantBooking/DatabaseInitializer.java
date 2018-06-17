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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Map<DayOfWeek, OpenHours> openHoursMap = new HashMap<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            OpenHours day = null;
            try {
                day = new OpenHoursBuilder()
                        .openHour(sdf.parse("00:00"))
                        .closeHour(sdf.parse("00:00"))
                        .isClose(true)
                        .build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            openHoursMap.put(dayOfWeek, day);
        }
        restaurantService.addOpenHours(restaurant, openHoursMap);
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
            Map<DayOfWeek, OpenHours> map = restaurant.getOpenHoursMap();
            map.get(DayOfWeek.FRIDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.FRIDAY).setCloseHour(f.parse("23:30"));
            map.get(DayOfWeek.THURSDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.THURSDAY).setCloseHour(f.parse("22:30"));
            map.get(DayOfWeek.WEDNESDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.WEDNESDAY).setCloseHour(f.parse("22:30"));
            map.get(DayOfWeek.SATURDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.SATURDAY).setCloseHour(f.parse("20:30"));
            map.get(DayOfWeek.SUNDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.SUNDAY).setCloseHour(f.parse("19:30"));
            restaurantService.updateOpenHours(restaurant);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
