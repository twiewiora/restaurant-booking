package com.application.restaurantbooking;

import com.application.restaurantbooking.persistence.builder.*;
import com.application.restaurantbooking.persistence.model.*;
import com.application.restaurantbooking.persistence.service.*;
import com.application.restaurantbooking.utils.geocoding.GeocodeUtil;
import com.application.restaurantbooking.utils.geocoding.Localization;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.EnumMap;
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

    private ClientService clientService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private GeocodeUtil geocodeUtil;

    @Autowired
    public DatabaseInitializer(RestorerService restorerService,
                               RestaurantService restaurantService,
                               RestaurantTableService restaurantTableService,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               ReservationService reservationService,
                               ClientService clientService,
                               GeocodeUtil geocodeUtil) {
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.reservationService = reservationService;
        this.clientService = clientService;
        this.geocodeUtil = geocodeUtil;
    }

    public void initializeDatabase() {
        String cityName = "Krakow";
        if (restorerService.getByUsername("test") != null) {
            return;
        }
        Restorer restorer = createRestorer("test", "test1");
        Client client = createClient("client", "client");
        Restaurant restaurant = createRestaurant(restorer, cityName, "sw. Marka", "22", Sets.newHashSet(Tag.PIZZA, Tag.KEBAB, Tag.DUMPLINGS), Price.LOW);

        RestaurantTable table1 = createRestaurantTable(restaurant, 12);
        RestaurantTable table2 = createRestaurantTable(restaurant, 5);
        RestaurantTable table3 = createRestaurantTable(restaurant, 2);
        RestaurantTable table4 = createRestaurantTable(restaurant, 7);
        RestaurantTable table5 = createRestaurantTable(restaurant, 4);
        RestaurantTable table6 = createRestaurantTable(restaurant, 3);

        createReservation(table1, "2018-09-14_22:30", 60, client);
        createReservation(table2, "2018-09-14_20:00", 180, client);
        createReservation(table3, "2018-09-14_19:00", 120, client);
        createReservation(table4, "2018-09-14_18:00", 60, client);
        createReservation(table5, "2018-09-14_17:00", 180, client);
        createReservation(table6, "2018-09-14_18:00", 120, client);

        createOpenHours(restaurant);
        restaurant = createRestaurant(restorer, cityName, "Mikolajska", "3", Sets.newHashSet(Tag.POLISH_CUISINE, Tag.DUMPLINGS), Price.HIGH);

        table1 = createRestaurantTable(restaurant, 12);
        table2 = createRestaurantTable(restaurant, 5);
        table3 = createRestaurantTable(restaurant, 2);
        table4 = createRestaurantTable(restaurant, 7);

        createReservation(table1, "2018-09-14_22:30", 60, client);
        createReservation(table2, "2018-09-14_20:00", 180, client);
        createReservation(table3, "2018-09-14_19:00", 120, client);
        createReservation(table4, "2018-09-14_18:00", 60, client);

        createOpenHours(restaurant);

        createOpenHours(createRestaurant(createRestorer("test2", "test2"), cityName, "Kawiory", "24", Sets.newHashSet(Tag.AMERICAN_CUISINE, Tag.MEXICAN_CUISINE), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test3", "test3"), cityName, "Lea", "34", Sets.newHashSet(Tag.ASIAN_CUISINE, Tag.SUSHI), Price.LOW));
        createOpenHours(createRestaurant(createRestorer("test4", "test4"), cityName, "Chopina", "33", Sets.newHashSet(Tag.SEAFOOD, Tag.SPANISH_CUISINE), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test5", "test5"), cityName, "Podchorazych", "2", Sets.newHashSet(Tag.BURGER, Tag.PIZZA), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test6", "test6"), cityName, "Karmelicka", "6", Sets.newHashSet(Tag.FAST_FOOD, Tag.ASIAN_CUISINE), Price.MEDIUM));
        createOpenHours(createRestaurant(createRestorer("test7", "test7"), cityName, "Straszewskiego", "16", Sets.newHashSet(Tag.SEAFOOD, Tag.FISH), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test8", "test8"), cityName, "Rynek Glowny", "19", Sets.newHashSet(Tag.HUNGARIAN_CUISINE), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test9", "test9"), cityName, "Rynek Glowny", "17", Sets.newHashSet(Tag.VEGETARIAN_CUISINE, Tag.FISH), Price.LOW));
        createOpenHours(createRestaurant(createRestorer("test10", "test10"), cityName, "Grodzka", "40", Sets.newHashSet(Tag.ITALIAN_CUISINE, Tag.PASTA), Price.LOW));
        createOpenHours(createRestaurant(createRestorer("test11", "test11"), cityName, "Stradomska", "11", Sets.newHashSet(Tag.MEXICAN_CUISINE), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test12", "test12"), cityName, "Grodzka", "5", Sets.newHashSet(Tag.GREEK_CUISINE), Price.LOW));
        createOpenHours(createRestaurant(createRestorer("test13", "test13"), cityName, "Sienna", "12", Sets.newHashSet(Tag.GERMAN_CUISINE), Price.MEDIUM));
        createOpenHours(createRestaurant(createRestorer("test14", "test14"), cityName, "Kanonicza", "15", Sets.newHashSet(Tag.POLISH_CUISINE, Tag.DUMPLINGS), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test15", "test15"), cityName, "sw. Tomasza", "15", Sets.newHashSet(Tag.SEAFOOD, Tag.SUSHI), Price.LOW));
        createOpenHours(createRestaurant(createRestorer("test16", "test16"), cityName, "Pijarska", "9", Sets.newHashSet(Tag.FIT_FOOD, Tag.FRENCH_CUISINE), Price.HIGH));
        createOpenHours(createRestaurant(createRestorer("test17", "test17"), cityName, "Miodowa", "25", Sets.newHashSet(Tag.CHINESE_CUISINE, Tag.ASIAN_CUISINE), Price.MEDIUM));
        createOpenHours(createRestaurant(createRestorer("test18", "test18"), cityName, "Rynek Glowny", "3", Sets.newHashSet(Tag.FAST_FOOD, Tag.PIZZA), Price.LOW));
        createOpenHours(createRestaurant(createRestorer("test19", "test19"), cityName, "Slawkowska", "17", Sets.newHashSet(Tag.AMERICAN_CUISINE, Tag.BURGER), Price.MEDIUM));
    }

    private Restorer createRestorer(String userName, String password) {
        Restorer restorer = new RestorerBuilder()
                .username(userName)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        return restorerService.createRestorer(restorer);
    }

    private Restaurant createRestaurant(Restorer restorer, String city, String street, String streetNumber, Set<Tag> tags, Price price) {
        Restaurant restaurant = new RestaurantBuilder()
                .name("name")
                .city(city)
                .street(street)
                .streetNumber(streetNumber)
                .phoneNumber("123")
                .price(price)
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

    private Reservation createReservation(RestaurantTable restaurantTable, String date, Integer length, Client client) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            Reservation reservation = new ReservationBuilder()
                    .client(client)
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
        String startHour = "12:30";
        try {
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            Map<DayOfWeek, OpenHours> map = restaurant.getOpenHoursMap();
            map.get(DayOfWeek.FRIDAY).setOpenHour(f.parse(startHour));
            map.get(DayOfWeek.FRIDAY).setCloseHour(f.parse("23:30"));
            map.get(DayOfWeek.FRIDAY).setClose(false);
            map.get(DayOfWeek.THURSDAY).setOpenHour(f.parse(startHour));
            map.get(DayOfWeek.THURSDAY).setCloseHour(f.parse("22:30"));
            map.get(DayOfWeek.THURSDAY).setClose(false);
            map.get(DayOfWeek.WEDNESDAY).setOpenHour(f.parse(startHour));
            map.get(DayOfWeek.WEDNESDAY).setCloseHour(f.parse("22:30"));
            map.get(DayOfWeek.WEDNESDAY).setClose(false);
            map.get(DayOfWeek.SATURDAY).setOpenHour(f.parse(startHour));
            map.get(DayOfWeek.SATURDAY).setCloseHour(f.parse("20:30"));
            map.get(DayOfWeek.SATURDAY).setClose(false);
            map.get(DayOfWeek.SUNDAY).setOpenHour(f.parse(startHour));
            map.get(DayOfWeek.SUNDAY).setCloseHour(f.parse("19:30"));
            map.get(DayOfWeek.SUNDAY).setClose(false);
            restaurantService.updateOpenHours(restaurant);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    private Client createClient(String username, String password) {
        Client client = new ClientBuilder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
        return clientService.createClient(client);
    }
}
