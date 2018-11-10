package com.application.restaurantbooking;

import com.application.restaurantbooking.persistence.builder.*;
import com.application.restaurantbooking.persistence.model.*;
import com.application.restaurantbooking.persistence.service.*;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    private RestorerService restorerService;

    private RestaurantService restaurantService;

    private RestaurantTableService restaurantTableService;

    private ClientService clientService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DatabaseInitializer(RestorerService restorerService,
                               RestaurantService restaurantService,
                               RestaurantTableService restaurantTableService,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               ClientService clientService) {
        this.restorerService = restorerService;
        this.restaurantService = restaurantService;
        this.restaurantTableService = restaurantTableService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clientService = clientService;
    }

    public void initializeDatabase() {
        if (restorerService.getByUsername("test") != null) {
            return;
        }
        createRestaurant("test", "50.06148585", "19.93641489", "Krakow", "Rynek Glowny", "1");
        URL url = Resources.getResource("restaurants.csv");
        try {
            CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
            BufferedReader reader = charSource.openBufferedStream();
            for (String line; (line = reader.readLine()) != null;) {
                String[] restaurantData = line.split(";");
                createRestaurant(restaurantData[2], restaurantData[0], restaurantData[1], restaurantData[3],
                        restaurantData[4], restaurantData[5]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Client client = new ClientBuilder()
                .username("client")
                .password(bCryptPasswordEncoder.encode("client"))
                .build();
        clientService.createClient(client);
    }

    private void createRestaurant(String name, String lat, String lon, String city, String street, String streetNumber) {
        Restorer restorer = new RestorerBuilder()
                .username(name.toLowerCase().replaceAll("\\.", "")
                .replaceAll(" ", ""))
                .password(bCryptPasswordEncoder.encode("test"))
                .build();
        restorerService.createRestorer(restorer);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Map<DayOfWeek, OpenHours> openHoursMap = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            OpenHours day = null;
            try {
                day = new OpenHoursBuilder()
                        .openHour(sdf.parse("12:00"))
                        .closeHour(sdf.parse("22:00"))
                        .isClose(false)
                        .build();
            } catch (ParseException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
            openHoursMap.put(dayOfWeek, day);
        }
        Restaurant restaurant = new RestaurantBuilder()
                .restorer(restorer)
                .name(name)
                .city(city)
                .street(street)
                .streetNumber(streetNumber)
                .phoneNumber("123456789")
                .price(Price.values()[new Random().nextInt(Price.values().length)])
                .website("http://restaurant.com")
                .tags(Sets.newHashSet(Tag.values()[new Random().nextInt(Tag.values().length)], Tag.values()[new Random().nextInt(Tag.values().length)]))
                .openHours(openHoursMap)
                .localization(Double.parseDouble(lat), Double.parseDouble(lon))
                .build();
        restaurantService.addOpenHours(restaurant, openHoursMap);

        for (int i = 2; i <= 10; i+=2) {
            createRestaurantTable(restaurant, i);
        }
    }

    private void createRestaurantTable(Restaurant restaurant, Integer places) {
        RestaurantTable restaurantTable = new RestaurantTableBuilder()
                .restaurant(restaurant)
                .identifier("ID" + places)
                .comment("Table for " + places)
                .maxPlaces(places)
                .build();
        restaurantTableService.createRestaurantTable(restaurantTable);
    }

}
