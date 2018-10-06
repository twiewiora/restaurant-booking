package com.application.restaurantbooking;

import com.application.restaurantbooking.persistence.builder.*;
import com.application.restaurantbooking.persistence.model.*;
import com.application.restaurantbooking.persistence.service.ReservationService;
import com.application.restaurantbooking.persistence.service.RestaurantService;
import com.application.restaurantbooking.persistence.service.RestaurantTableService;
import com.application.restaurantbooking.persistence.service.RestorerService;
import com.application.restaurantbooking.utils.geocoding.GeocodeUtil;
import com.application.restaurantbooking.utils.geocoding.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    private RestorerService restorerService;

    private RestaurantService restaurantService;

    private RestaurantTableService restaurantTableService;

    private ReservationService reservationService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private GeocodeUtil geocodeUtil;

    @Autowired
    public DatabaseInitializer(RestorerService restorerService,
                               RestaurantService restaurantService,
                               RestaurantTableService restaurantTableService,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               ReservationService reservationService,
                               GeocodeUtil geocodeUtil) {
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.reservationService = reservationService;
        this.geocodeUtil = geocodeUtil;
    }

    public void initializeDatabase() {
        Restorer restorer = createRestorer("test", "test1");
        Restaurant restaurant = createRestaurant(restorer, "Nowy Sacz", "Piastowska", "3");

        RestaurantTable table1 = createRestaurantTable(restaurant, 12);
        RestaurantTable table2 = createRestaurantTable(restaurant, 5);
        RestaurantTable table3 = createRestaurantTable(restaurant, 2);
        RestaurantTable table4 = createRestaurantTable(restaurant, 7);
        RestaurantTable table5 = createRestaurantTable(restaurant, 4);
        RestaurantTable table6 = createRestaurantTable(restaurant, 3);

        createReservation(table1, "2018-09-14_22:30", 1);
        createReservation(table2, "2018-09-14_20:00", 3);
        createReservation(table3, "2018-09-14_19:00", 2);
        createReservation(table4, "2018-09-14_18:00", 1);
        createReservation(table5, "2018-09-14_17:00", 3);
        createReservation(table6, "2018-09-14_18:00", 2);

        createOpenHours(restaurant);

        createOpenHours(createRestaurant(createRestorer("test2", "test2"), "Kraków", "Kawiory", "24"));
        createOpenHours(createRestaurant(createRestorer("test3", "test3"), "Kraków", "Lea", "34"));
        createOpenHours(createRestaurant(createRestorer("test4", "test4"), "Kraków", "Chopina", "33"));
        createOpenHours(createRestaurant(createRestorer("test5", "test5"), "Kraków", "Podchorążych", "2"));
        createOpenHours(createRestaurant(createRestorer("test6", "test6"), "Kraków", "Karmelicka", "6"));
    }

    private Restorer createRestorer(String userName, String password) {
        Restorer restorer = new RestorerBuilder()
                .username(userName)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        return restorerService.createRestorer(restorer);
    }

    private Restaurant createRestaurant(Restorer restorer, String city, String street, String streetNumber) {
        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.KEBAB);
        tags.add(Tag.PIZZA);
        Restaurant restaurant = new RestaurantBuilder()
                .name("name")
                .city(city)
                .street(street)
                .streetNumber(streetNumber)
                .phoneNumber("123")
                .restorer(restorer)
                .tags(tags)
                .build();
        Localization localization = geocodeUtil.getLocalizationByAddress(city, street, streetNumber);
        restaurant.setLongitude(localization.getLongitude());
        restaurant.setLatitude(localization.getLatitude());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Map<DayOfWeek, OpenHours> openHoursMap = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            OpenHours day = null;
            try {
                day = new OpenHoursBuilder()
                        .openHour(sdf.parse("00:00"))
                        .closeHour(sdf.parse("00:00"))
                        .isClose(true)
                        .build();
            } catch (ParseException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
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
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    private void createOpenHours(Restaurant restaurant) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            Map<DayOfWeek, OpenHours> map = restaurant.getOpenHoursMap();
            map.get(DayOfWeek.FRIDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.FRIDAY).setCloseHour(f.parse("23:30"));
            map.get(DayOfWeek.FRIDAY).setClose(false);
            map.get(DayOfWeek.THURSDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.THURSDAY).setCloseHour(f.parse("22:30"));
            map.get(DayOfWeek.THURSDAY).setClose(false);
            map.get(DayOfWeek.WEDNESDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.WEDNESDAY).setCloseHour(f.parse("22:30"));
            map.get(DayOfWeek.WEDNESDAY).setClose(false);
            map.get(DayOfWeek.SATURDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.SATURDAY).setCloseHour(f.parse("20:30"));
            map.get(DayOfWeek.SATURDAY).setClose(false);
            map.get(DayOfWeek.SUNDAY).setOpenHour(f.parse("12:30"));
            map.get(DayOfWeek.SUNDAY).setCloseHour(f.parse("19:30"));
            map.get(DayOfWeek.SUNDAY).setClose(false);
            restaurantService.updateOpenHours(restaurant);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
