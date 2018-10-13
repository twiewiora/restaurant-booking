package com.application.restaurantbooking;

import com.application.restaurantbooking.persistence.builder.RestaurantBuilder;
import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Price;
import com.application.restaurantbooking.persistence.model.Restaurant;
import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.utils.RestaurantSearcher;
import com.application.restaurantbooking.utils.RestaurantSearcherRequest;
import com.application.restaurantbooking.utils.geocoding.Localization;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class RestaurantSearcherTest {

    private RestaurantSearcher restaurantSearcher;

    private Restaurant restaurant1;

    private Restaurant restaurant2;

    private Restaurant restaurant3;

    private List<Restaurant> restaurants = new ArrayList<>();

    private Set<Tag> tags = new HashSet<>();

    private Localization clientLocalization = new Localization(50.0709715, 19.9156061);

    private Client client = new Client();

    @Mock
    private ClientService clientService;

    public RestaurantSearcherTest() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(clientService.getStatisticsForTag(1L, Tag.PIZZA)).thenReturn(2);
        Mockito.when(clientService.getStatisticsForTag(1L, Tag.KEBAB)).thenReturn(5);
        Mockito.when(clientService.getStatisticsForPrice(1L, Price.HIGH)).thenReturn(4);
        Mockito.when(clientService.getStatisticsForRestaurant(1L, 1L)).thenReturn(5);
        Mockito.when(clientService.getStatisticsForRestaurant(1L, 2L)).thenReturn(5);
        Mockito.when(clientService.getStatisticsForRestaurant(1L, 3L)).thenReturn(5);
        Mockito.when(clientService.getAmountClientReservations(1L)).thenReturn(10);

        Localization localization1 = new Localization(50.0680966, 19.9125399);
        Localization localization2 = new Localization(50.0679589, 19.9186125);
        Localization localization3 = new Localization(50.0737619, 19.9086375);
        restaurantSearcher = new RestaurantSearcher(clientService);
        restaurant1 = createRestaurant("Kawiory", "21");
        restaurant1.setLatitude(localization1.getLatitude());
        restaurant1.setLongitude(localization1.getLongitude());
        restaurant1.setId(1L);
        restaurant1.setPrice(Price.HIGH);
        restaurant2 = createRestaurant( "Chopina", "33");
        restaurant2.setLatitude(localization2.getLatitude());
        restaurant2.setLongitude(localization2.getLongitude());
        restaurant2.setId(2L);
        restaurant2.setPrice(Price.HIGH);
        restaurant3 = createRestaurant( "Podchorążych", "2");
        restaurant3.setLatitude(localization3.getLatitude());
        restaurant3.setLongitude(localization3.getLongitude());
        restaurant3.setId(3L);
        restaurant3.setPrice(Price.HIGH);
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);
        restaurants.add(restaurant3);
        tags.add(Tag.PIZZA);
        tags.add(Tag.KEBAB);
        client.setId(1L);
    }

    private Restaurant createRestaurant(String street, String streetNumber) {
        String city = "Kraków";
        Restaurant restaurant = new RestaurantBuilder().city(city).street(street).streetNumber(streetNumber).build();
        restaurant.setTags(tags);
        return restaurant;
    }

    @Test
    public void getSurroundingRestaurantsTest1() {
        RestaurantSearcherRequest request = new RestaurantSearcherRequest(clientLocalization, 400, tags);
        List<Restaurant> result = restaurantSearcher.getSurroundingRestaurant(restaurants, request, client);
        assertEquals(Arrays.asList(restaurant1, restaurant2), result);
    }

    @Test
    public void getSurroundingRestaurantsTest2() {
        RestaurantSearcherRequest request = new RestaurantSearcherRequest(clientLocalization, 390, tags);
        List<Restaurant> result = restaurantSearcher.getSurroundingRestaurant(restaurants, request, client);
        assertEquals(Collections.singletonList(restaurant1), result);
    }

    @Test
    public void getSurroundingRestaurantsTest3() {
        RestaurantSearcherRequest request = new RestaurantSearcherRequest(clientLocalization, 600, tags);
        List<Restaurant> result = restaurantSearcher.getSurroundingRestaurant(restaurants, request, client);
        assertEquals(Arrays.asList(restaurant1, restaurant2, restaurant3), result);
    }
}
